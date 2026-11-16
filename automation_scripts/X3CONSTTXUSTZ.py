# X3CONSTTXUSTZ
# Custom rule handler del connettore MAXIMO-SAP per lo stato delle Sedi Tecniche delle STAZIONI
# Lo script ritorna lo stato mappato con SAP
#   -Per primi e terzi livelli ritorna lo stato dell'asset attuale.
#   -Per secondi e quarti livelli ritorna lo stato del padre.
#   -Per alcune classi particolari equipment SAP di quarto livello ritorna lo stato dell'asset attuale.
# v. 1.1

from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

# Mappa degli stati SAP-EAM
statusMap = {
    "DATT": "ESER",
    "ATTV": "ATTV",
    "NADI": "NADI",
    "NAND": "NAND",
    "CEAL": "CEAL",
    "DEMO": "DEMO",
    "DELE": "DEMO"
}

# Mappa stati particolari equipment
statusMapEq = {
    "DELE": "DIST"
}

# Mappa stati IMPIANTO DI TERRA/MAGLIA DI TERRA/MAGLIA
statusMapImp = {
    "DATT": "DATT",
    "DELE": "DEMO",
    "ESER": "ATTV",
    "EFFE": "ATTV",
    "EFDI": "ATTV",
    "EFPR": "ATTV",
    "EFSC": "ATTV",
    "GUAS": "ATTV",
    "NIED": "NADI",
    "NIEP": "NADI",
    "NIES": "NADI",
    "NIEO": "NADI",
    "NIGU": "NADI",
    "RIPR": "NADI"
}

# Livello e classe SAP
sapLevel = sapAsset.getInt("SAPLEVEL")
sapClass = sapAsset.getString("sapclass")
sapObject = sapAsset.getString("sapobject")

# Clausola where generale valida per tutti i livelli
whereClause = "(siteid, assetnum) in (select siteid, objectnum from X3SAPTOMAXOBJECT where sapassetid=:x3sapassetid AND hierarchypath IN (SELECT xm.hierarchypath FROM X3MAXIMOSAPMAP xm WHERE xm.sapclass = :sapclass and step2target = 'ASSET_AND_BINDING'))"

specialClassMap = {
    "BOOSTER": "MACCHINA BOOSTER",
    "PHASE SHIFTING TRANSFORMER": "MACCHINA PHASE SHIFTING TRANSFORMER",
    "PASSANTE AT ARIA - ARIA": "PASSANTE AIS",
    "REATTORE": "MACCHINA REATTORE DI RIFASAMENTO",
    "TRASFORMATORE A 3 AVVOLGIMENTI": "MACCHINA TRASFORMATORE A 3 AVVOLGIMENTI",
    "TRASFORMATORE AT/MT": "MACCHINA TRASFORMATORE AT/MT",
    "TRASFORMATORE DI GRUPPO": "MACCHINA TRASFORMATORE DI GRUPPO",
    "TRASFORMATORE DI INTERCONNESSIONE": "MACCHINA TRASFORM. DI INTERCONNESSIONE",
    "TRASFORMATORE MT/MT": "MACCHINA TRASFORMATORE MT/MT",
    "TRASFORMATORE AT/BT ISOLATO IN OLIO": "MACCHINA TRASFORMATORE AT/BT IN OLIO"
}

specialClassEq = [
    "REGISTRATORE CRONOLOGICO DI EVENTI",
    "R.I.P.E.S.",
    "SISTEMA REGISTRAZIONE PERTURBAZIONI (UC)",
    "APPARATO OCV ALTA FREQUENZA",
    "APPARATO DI TELECOMANDO",
    "AUTOMATISMO DI STAZIONE (ASAT)"
]

if sapClass in specialClassMap:
    whereClause = "(assetnum, siteid) in (select objectnum, siteid from x3saptomaxobject where :x3sapassetid = sapassetid and classificationid = '%s')" % specialClassMap[sapClass]

assetMboSet = sapAsset.getMboSet("$ASSETREL2", "ASSET", whereClause)

if assetMboSet.count() == 0:
    ruleHandler.addSimpleWarningForRule(mappingRule)
else:
    assetMbo = assetMboSet.moveFirst() # asset attuale

    # Gestione classi particolari
    if sapClass in specialClassEq:
        mappedRawValue = assetMbo.getString("STATUS")

    # Per secondi livelli non equipment prendo l'asset padre
    elif sapLevel == 2 and not sapObject == "E":
        assetMboSetParent = assetMbo.getMboSet("$PARENTASSET", "ASSET", "assetnum=:parent and siteid=:siteid")

        if assetMboSetParent.count() == 0:
            ruleHandler.addSimpleWarningForRule(mappingRule)
        else:
            assetMbo = assetMboSetParent.moveFirst()
            mappedRawValue = statusMap[assetMbo.getString("STATUS")]

    # Equipment standard
    elif sapLevel == 5 or (sapLevel == 2 and sapObject == "E"):
        if assetMbo.getString("STATUS") == "DELE":
            mappedRawValue = statusMapEq[assetMbo.getString("STATUS")]
        else:
            mappedRawValue = assetMbo.getString("STATUS")

    # Impianto di terra / Maglia di terra
    elif sapClass == "IMPIANTO DI TERRA":
        mappedRawValue = statusMapImp[assetMbo.getString("STATUS")]

    # Gestione standard
    else:
        mappedRawValue = statusMap[assetMbo.getString("STATUS")]