from psdi.server import MXServer
from com.ibm.json.java import JSONObject, JSONArray
from psdi.mbo import SqlFormat

# =============================================================================
# CONFIGURAZIONE
# =============================================================================

IGNORED_FIELDS = set([
    "USERID",
    "BODY",
    "ACTION",
    "_ACTION",
    "CHANGEBY",
    "CHANGEDATE",
    "ROWSTAMP",
    "_ROWSTAMP",
    "LANGCODE",
    "ORGID"
])


# Oggetto principale.
# Nel JSON flat di MxLoader normalmente i campi arrivano senza prefisso.
MAIN_OBJECTS = set([
    "WORKORDER"
])


# Child object.
# Se nel JSON i child arrivano come oggetti/array annidati, vengono prefissati.
CHILD_OBJECTS = set([
    "WPLABOR",
    "WPSERVICE"
])


COMMON_OPTIONAL_FIELDS = set([
    # Optional validi per tutte le combinazioni, se servono.
    # Meglio usarli poco.
])

COMBOS_SKIP_WONUM_EXISTENCE_CHECK = set([
    "MXLOADER_CREAZIONE_BASE",
    "MXLOADER_CREAZIONE_CON_POLICY",
    "MXLOADER_CREAZIONE_CON_POLICY_MOD",
    "COLWO_NEW_SG_ACN_V2"
])
REQUIRED_COMBINATIONS = [
    {
        "name": "MXLOADER_COIN",
        "required": [
            "WONUM",
            "SITEID",
            "X4TIPOSPESA",
            "X4COIN",
        ],
        "optional": [
            "X4WBS",
            "X4WBE",
            "IDCLUSTER",
            "OWNERGROUP"
        ]
    },
    {
        "name": "MXLOADER_PMI",
        "required": [
            "WONUM",
            "SITEID",
            "X4PMI"
        ],
        "optional": [
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_DATIODL",
        "required": [
            "WONUM",
            "SITEID",
            "ESTDUR",
            "X4BURKS",
            "X4OUTSOURCING"
        ],
        "optional": [
            "X4NATURALAVORO",
            "X4NOTE",
            "IDCLUSTER",
            "X4TTRASF",
            "JPNUM"
        ]
    },
    {
        "name": "MXLOADER_RISEST",
        "required": [
            "WONUM",
            "SITEID",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "WPSERVICE.ITEMNUM",
            "WPSERVICE.ITEMQTY",
            "WPSERVICE.LINECOST",
            "WPSERVICE.X4DATAINIZIORISORSA",
            "WPSERVICE.X4DATAFINERISORSA"
        ],
        "optional": [
            "WPSERVICE.WPITEMID",
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_RISINT",
        "required": [
            "WONUM",
            "SITEID",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "ESTDUR",
            "WPLABOR.CRAFT",
            "WPLABOR.QUANTITY",
            "WPLABOR.X4TITRIS",
            "WPLABOR.LABORHRS",
            "WPLABOR.X4DATAINIZIORISORSA",
            "WPLABOR.X4DATAFINERISORSA"
        ],
        "optional": [
            "WPLABOR.WPLABORUID",
            "WPLABOR.X4TITRIS",
            "WPLABOR.X4ORETRASF",
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_CREAZIONE_ODL_CHIUSI_V2",
        "required": [
            "WONUM",
            "SITEID",
            "DESCRIPTION",
            "X4AGGREGATI",
            "STATUS",
            "ASSETNUM",
            "JPNUM",
            "X4FISICOPLAN",
            "X4FISICO",
            "OWNERGROUP",
            "X4TIPOSPESA",
            "X4COIN",
            "X4BURKS",
            "X4PERIMETROINT",
            "X4TIPOODL",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "ACTFINISH",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "X4POLICY",
            "X4SAPIDASSET",
            "PARENT",
            "X4POLICYID",
            "X4WBE",
            "WOEQ9",
            "X4WBS",
            "X4COIN",
            "X4PRIMAELEGGIBILITA",
            "IDCLUSTER",
            "REPORTDATE"
        ]
    },
    {
        "name": "MXLOADER_CREAZIONE_BASE",
        "required": [
            "ASSETNUM",
            "SITEID",
            "JPNUM",
            "X4TIPOSPESA",
            "X4COIN",
            "X4PMI",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT",
            "X4BURKS",
            "X4OUTSOURCING"
        ],
        "optional": [
            "WONUM",
            "X4SOTTOPROGRAMMA",
            "X4NATURALAVORO",
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_CREAZIONE_CON_POLICY",
        "required": [
            "WONUM",
            "ASSETNUM",
            "SITEID",
            "JPNUM",
            "X4TIPOSPESA",
            "X4COIN",
            "X4PMI",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT",
            "X4BURKS",
            "X4OUTSOURCING"
        ],
        "optional": [
            "PARENT",
            "X4POLICY",
            "X4POLICYID",
            "X4DATAMODELLO",
            "X4TRONCONUM",
            "X4AGGREGATI",
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_CREAZIONE_CON_POLICY_MOD",
        "required": [
            "WONUM",
            "ASSETNUM",
            "SITEID",
            "JPNUM",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "PARENT",
            "X4COIN",
            "X4POLICY",
            "X4POLICYID",
            "X4SAPIDASSET",
            "X4DATAMODELLO",
            "X4TRONCONUM",
            "X4AGGREGATI",
            "IDCLUSTER",
            "X4PRIMAELEGGIBILITA",
            "X4WBS",
            "X4WBE"
        ]
    },
    {
        "name": "MXLOADER_DATE",
        "required": [
            "WONUM",
            "SITEID",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "IDCLUSTER",
            "REPORTDATE"
        ]
    },
    {
        "name": "MXLOADER_DATEPADRE",
        "required": [
            "WONUM",
            "SITEID",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
        ],
        "optional": [
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_SERB",
        "required": [
            "WONUM",
            "SITEID",
            "X4FISICOPLAN"
        ],
        "optional": [
            "IDCLUSTER"
        ]
    },
    {
        "name": "COLWO_NEW_SG_ACN_V2",
        "required": [
            "WONUM",
            "ASSETNUM",
            "SITEID",
            "JPNUM",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "X4POLICYID",
            "PARENT",
            "X4SAPIDASSET",
            "X4AGGREGATI",
            "X4TRONCONUM",
            "X4DATAMODELLO",
            "X4POLICY",
            "X4POLICYID",
            "IDCLUSTER",
            "X4PRIMAELEGGIBILITA",
            "REPORTDATE"
        ]
    },
    {
        "name": "MXLOADER_IMPORT_MISURE",
        "required": [
            "ASSETNUM",
            "IDMISURA_EXT",
            "IDMISURA",
            "ULTIMARILEVAZIONE",
            "PRIMARILEVAZIONE",
            "VALORE",
            "SITEID"
        ],
        "optional": [
            # Popola qui
        ]
    },
    {
        "name": "MXLOADER_DIFFOR_INTERF",
        "required": [
            "IDDIFFORMITA_EXT",
            "ASSETNUM",
            "STATUS",
            "PRIMARILEVAZIONE",
            "ULTIMARILEVAZIONE",
            "SITEID"
        ],
        "optional": [
            "DESCRIPTION",
            "FASE",
            "GRAVITA",
            "IDMISURA",
            "VALMISURA"
        ]
    },
    {
        "name": "GIN",
        "required": [
            "WONUM",
            "SITEID",
            "REASONFORCHANGE"
        ],
        "optional": [
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_4in1",
        "required": [
            "WONUM",
            "SITEID",
            "X4TIPOSPESA",
            "X4COIN",
			"X4PMI",
			"ESTDUR",
            "X4BURKS",
            "X4OUTSOURCING",
			"TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "X4WBS",
            "X4WBE",
            "IDCLUSTER",
            "OWNERGROUP",
			"X4NATURALAVORO",
            "X4NOTE",
            "IDCLUSTER",
            "X4TTRASF",
            "JPNUM",
			"REPORTDATE"
        ]
    }
]


# =============================================================================
# UTILITY
# =============================================================================

def get_json_value_case_insensitive(json_obj, field_name):
    if json_obj is None:
        return None

    target = normalize(field_name)

    keys = json_obj.keySet().iterator()

    while keys.hasNext():
        key = keys.next()

        if normalize(key) == target:
            return json_obj.get(key)

    return None


def get_json_string_value(json_obj, field_name):
    value = get_json_value_case_insensitive(json_obj, field_name)

    if is_blank(value):
        return ""

    return str(value).strip()



def normalize(value):
    if value is None:
        return ""

    return str(value).strip().upper()


def is_blank(value):
    if value is None:
        return True

    s = str(value).strip()

    if s == "":
        return True

    # Se vuoi considerare null string come vuoto
    if s.upper() == "NULL":
        return True

    return False


def safe_preview(value, max_len):
    if value is None:
        return "None"

    try:
        s = unicode(value)
    except:
        s = str(value)

    s = s.replace("\r", "\\r")
    s = s.replace("\n", "\\n")
    s = s.replace("\t", "\\t")

    if len(s) > max_len:
        return s[:max_len] + "..."

    return s


def clean_body_for_json(value):
    if value is None:
        return ""

    try:
        s = unicode(value)
    except:
        s = str(value)

    if s.startswith(u"\ufeff"):
        s = s[1:]

    s = s.strip()

    # Se per qualche motivo c'è testo prima del JSON, taglia dal primo {
    first_json_char = s.find("{")

    if first_json_char > 0:
        s = s[first_json_char:]

    return s


def normalize_field_list(values):
    result = set()

    for value in values:
        norm = normalize(value)

        if norm:
            result.add(norm)

    return result


def get_java_class_name(value):
    if value is None:
        return ""

    try:
        return value.getClass().getName()
    except:
        return ""


def is_json_object(value):
    if value is None:
        return False

    try:
        if isinstance(value, JSONObject):
            return True
    except:
        pass

    return get_java_class_name(value) == "com.ibm.json.java.JSONObject"


def is_json_array(value):
    if value is None:
        return False

    try:
        if isinstance(value, JSONArray):
            return True
    except:
        pass

    return get_java_class_name(value) == "com.ibm.json.java.JSONArray"

def build_field_name(current_object, attr_name):
    attr_name = normalize(attr_name)

    if "." in attr_name:
        return attr_name

    if current_object is None:
        return attr_name

    current_object = normalize(current_object)

    if current_object in MAIN_OBJECTS:
        return attr_name

    if current_object in CHILD_OBJECTS:
        return current_object + "." + attr_name

    return attr_name


# =============================================================================
# PARSING JSON
# =============================================================================

def parse_json(json_string):
    return JSONObject.parse(json_string)


def extract_populated_fields_from_json(json_string):
    populated_fields = set()

    root = parse_json(json_string)

    def walk_json_object(json_obj, current_object):
        keys = json_obj.keySet().iterator()

        while keys.hasNext():
            key = keys.next()
            raw_key = str(key)
            norm_key = normalize(raw_key)

            value = json_obj.get(key)

            if norm_key in IGNORED_FIELDS:
                continue

            # Caso child object annidato:
            # "wplabor": { ... }
            # oppure:
            # "wplabor": [ { ... }, { ... } ]
            if norm_key in CHILD_OBJECTS or norm_key in MAIN_OBJECTS:
                if is_json_object(value):
                    walk_json_object(value, norm_key)
                    continue

                if is_json_array(value):
                    for i in range(value.size()):
                        item = value.get(i)

                        if is_json_object(item):
                            walk_json_object(item, norm_key)

                    continue

            # Caso oggetto JSON generico annidato
            if is_json_object(value):
                walk_json_object(value, current_object)
                continue

            # Caso array generico
            if is_json_array(value):
                for i in range(value.size()):
                    item = value.get(i)

                    if is_json_object(item):
                        walk_json_object(item, current_object)
                    else:
                        if not is_blank(item):
                            field_name = build_field_name(current_object, norm_key)

                            if field_name not in IGNORED_FIELDS:
                                populated_fields.add(field_name)

                continue

            # Caso campo semplice
            if not is_blank(value):
                field_name = build_field_name(current_object, norm_key)

                if field_name not in IGNORED_FIELDS:
                    populated_fields.add(field_name)

    walk_json_object(root, None)

    return populated_fields





# =============================================================================
# SCRIPT OSIN
# =============================================================================

def beforeMboData(ctx):
    # =============================================================================
    # VALIDAZIONE
    # =============================================================================
    def validate_aln_domain_value(json_body, field_name, domain_id, userInfo):
        root = parse_json(json_body)
    
        value = get_json_value_case_insensitive(root, field_name)
    
        # Se il campo non è presente o è vuoto, lascio gestire il required template.
        if is_blank(value):
            return
    
        value_str = str(value).strip()
    
        alnSet = None
    
        try:
            alnSet = MXServer.getMXServer().getMboSet("ALNDOMAIN", userInfo)
    
            sqlf = SqlFormat(userInfo, "domainid = :1 and value = :2")
            sqlf.setObject(1, "ALNDOMAIN", "DOMAINID", domain_id)
            sqlf.setObject(2, "ALNDOMAIN", "VALUE", value_str)
    
            alnSet.setWhere(sqlf.format())
            alnSet.reset()
    
            if alnSet.getMbo(0) is None:
                ctx.error("x3terna", "ErrorMsg", [
                    field_name + " non valido: '" + value_str + "'. Il valore non risulta ammesso dal dominio."
                ])
    
        finally:
            if alnSet is not None:
                alnSet.close()
    
    def is_true_outsourcing_value(value):
        if is_blank(value):
            return False
    
        v = normalize(value)
    
        return v in ("1", "TRUE", "YES", "Y")
    
    def validate_naturalavoro_required_if_outsourcing(json_body, combo_name):
        combo_name_norm = normalize(combo_name)
    
        combos_with_outsourcing = set([
            "MXLOADER_DATIODL",
            "MXLOADER_CREAZIONE_BASE",
            "MXLOADER_CREAZIONE_CON_POLICY"
        ])
    
        if combo_name_norm not in combos_with_outsourcing:
            return
    
        root = parse_json(json_body)
    
        outsourcing = get_json_value_case_insensitive(root, "X4OUTSOURCING")
    
        if not is_true_outsourcing_value(outsourcing):
            return
    
        natura_lavoro = get_json_value_case_insensitive(root, "X4NATURALAVORO")
    
        if is_blank(natura_lavoro):
            ctx.error("x3terna", "ErrorMsg", [
                "X4NATURALAVORO e obbligatorio quando X4OUTSOURCING e valorizzato a 1, true, Yes o Y."
            ])
    def workorder_exists(wonum, siteid, userInfo):
        woSet = None
    
        try:
            woSet = MXServer.getMXServer().getMboSet("WORKORDER", userInfo)
    
            if not is_blank(siteid):
                sqlf = SqlFormat(userInfo, "wonum = :1 and siteid = :2")
                sqlf.setObject(1, "WORKORDER", "WONUM", wonum)
                sqlf.setObject(2, "WORKORDER", "SITEID", siteid)
            else:
                sqlf = SqlFormat(userInfo, "wonum = :1")
                sqlf.setObject(1, "WORKORDER", "WONUM", wonum)
    
            woSet.setWhere(sqlf.format())
            woSet.reset()
    
            return woSet.getMbo(0) is not None
    
        finally:
            if woSet is not None:
                woSet.close()
    
    def validate_serbatoio_if_x4fisicoplan(json_body, combo_name, userInfo):
        if normalize(combo_name) != "MXLOADER_SERB":
            return
    
        root = parse_json(json_body)
    
        wonum = get_json_string_value(root, "WONUM")
        siteid = get_json_string_value(root, "SITEID")
        x4fisicoplan = get_json_value_case_insensitive(root, "X4FISICOPLAN")
        
        try:
            fisicoplan_value = float(str(x4fisicoplan).strip().replace(",", "."))
        except:
            ctx.error("x3terna", "ErrorMsg", [
                "X4FISICOPLAN deve essere un valore numerico maggiore di 0."
            ])
            return
    
        if fisicoplan_value < 0:
            ctx.error("x3terna", "ErrorMsg", [
                "X4FISICOPLAN deve essere maggiore di 0."
            ])
            return
    
        woSet = None
    
        try:
            woSet = MXServer.getMXServer().getMboSet("WORKORDER", userInfo)
    
            sqlf = SqlFormat(userInfo, "wonum = :1 and siteid = :2")
            sqlf.setObject(1, "WORKORDER", "WONUM", wonum)
            sqlf.setObject(2, "WORKORDER", "SITEID", siteid)
    
            woSet.setWhere(sqlf.format())
            woSet.reset()
    
            wo = woSet.getMbo(0)
    
            if wo is not None and not wo.getBoolean("X4SERBATOIO"):
                ctx.error("x3terna", "ErrorMsg", [
                    "X4FISICOPLAN modificabile solo per ODL con X4SERBATOIO = 1."
                ])
    
        finally:
            if woSet is not None:
                woSet.close()
    
    def validate_wonum_exists_if_needed(json_body, combo_name, userInfo):
        combo_name_norm = normalize(combo_name)
    
        # Per queste combinazioni il WONUM può non esistere ancora,
        # perché sono template di creazione.
        if combo_name_norm in COMBOS_SKIP_WONUM_EXISTENCE_CHECK:
            return
    
        root = parse_json(json_body)
    
        wonum = get_json_string_value(root, "WONUM")
        siteid = get_json_string_value(root, "SITEID")
    
        # Se la combo non prevede WONUM, non controllo.
        # Se invece WONUM è required, la validazione combinazione lo ha già controllato prima.
        if is_blank(wonum):
            return
    
        if not workorder_exists(wonum, siteid, userInfo):
            if not is_blank(siteid):
                msg = "L'ODL " + wonum + " non esiste per il sito " + siteid + "."
            else:
                msg = "L'ODL " + wonum + " non esiste."
    
            ctx.error("x3terna", "ErrorMsg", [msg])
    def get_allowed_fields_for_combo(combo):
        required = normalize_field_list(combo["required"])
        optional = normalize_field_list(combo.get("optional", []))
    
        allowed = set()
        allowed.update(required)
        allowed.update(optional)
        allowed.update(COMMON_OPTIONAL_FIELDS)
        allowed.update(IGNORED_FIELDS)
    
        return allowed
    
    
    def validate_loader_body(json_body):
        sent_fields = extract_populated_fields_from_json(json_body)
    
        valid_combinations = []
        errors = []
    
        for combo in REQUIRED_COMBINATIONS:
            combo_name = combo["name"]

            required = normalize_field_list(combo["required"])
            allowed = get_allowed_fields_for_combo(combo)
    
            missing = required.difference(sent_fields)
            extra = sent_fields.difference(allowed)
    
            if len(missing) == 0 and len(extra) == 0:
                valid_combinations.append(combo_name)
            else:
                errors.append({
                    "combo": combo_name,
                    "missing": missing,
                    "extra": extra
                })
    
        if len(valid_combinations) > 0:
            return valid_combinations[0]
    
        best_error = None
        best_score = None
    
        for err in errors:
            score = len(err["missing"]) + len(err["extra"])
    
            if best_error is None or score < best_score:
                best_error = err
                best_score = score
    
        msg = "Template MxLoader non valido."
    
        if best_error is not None:

            if len(best_error["missing"]) > 0:
                msg += "\n\nCampi obbligatori mancanti"
    
            if len(best_error["extra"]) > 0:
                msg += "\n\nCampi valorizzati non ammessi per questa combinazione:"
                msg += "\n" + ", ".join(sorted(list(best_error["extra"])))
    
        ctx.error("x3terna", "ErrorMsg", [msg])
    
    data = ctx.getData()
    mbo = ctx.getMbo()

    mxServer = MXServer.getMXServer()

    currentUI = mbo.getUserInfo()
    userId = currentUI.getUserName()

    raw_body = data.getDataAsString()
    body = clean_body_for_json(raw_body)

    data.setCurrentData("USERID", userId)
    data.setCurrentData("BODY", raw_body)

    if body is None or body.strip() == "":
        ctx.error("x3terna", "ErrorMsg", [
            "Body MxLoader vuoto. Impossibile validare il template."
        ])

    if not body.startswith("{"):
        ctx.error("x3terna", "ErrorMsg", [
            "Body MxLoader non JSON. Primo contenuto ricevuto: " + safe_preview(raw_body, 500)
        ])

    try:
        combo_name = validate_loader_body(body)
        validate_aln_domain_value(body, "X4BURKS", "X3ZZSOCP", currentUI)
        validate_naturalavoro_required_if_outsourcing(body, combo_name)
        validate_wonum_exists_if_needed(body, combo_name, currentUI)
        validate_serbatoio_if_x4fisicoplan(body, combo_name, currentUI)
    except Exception, e:
        ctx.error("x3terna", "ErrorMsg", [
            "Errore durante la validazione del template MxLoader. "
            + "Body ricevuto: "
            + safe_preview(raw_body, 500)
            + " - Errore: "
            + str(e)
        ])