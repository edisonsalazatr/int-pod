#X3CONPOSNREQ
#Custom rule handler del connettore MAXIMO-SAP per il calcolo dell'attributo di testata degli equipment delle STAZIONI Posnr
#v 1.0

from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from psdi.server import MXServer


# Recupero della classe SAP dell'asset corrente
sapClass = sapAsset.getString("SAPCLASS")
hierarchypath = ""

# Gestione dei vari tipi di trasformatori e reattori
if sapClass == 'TRASFORMATORE DI GRUPPO':
    hierarchypath = "AND (HIERARCHYPATH='STAZIONE\\TRASFORMAZIONI\\TRASFORMATORE\\MACCHINA TRASFORMATORE DI GRUPPO' OR HIERARCHYPATH='STAZIONE\\MACCHINA TRASFORMATORE DI GRUPPO')"
elif sapClass == 'BOOSTER':
    hierarchypath = "AND (HIERARCHYPATH='STAZIONE\\TRASFORMAZIONI\\BOOSTER\\MACCHINA BOOSTER' OR HIERARCHYPATH='STAZIONE\\MACCHINA BOOSTER')"
elif sapClass == 'TRASFORMATORE A 3 AVVOLGIMENTI':
    hierarchypath = "AND (HIERARCHYPATH='STAZIONE\\TRASFORMAZIONI\\TRASFORMATORE\\MACCHINA TRASFORMATORE A 3 AVVOLGIMENTI' OR HIERARCHYPATH='STAZIONE\\MACCHINA TRASFORMATORE A 3 AVVOLGIMENTI')"
elif sapClass == 'REATTORE':
    hierarchypath = "AND (HIERARCHYPATH='STAZIONE\\RIFASAMENTI\\REATTORE\\MACCHINA REATTORE DI RIFASAMENTO' OR HIERARCHYPATH='STAZIONE\\MACCHINA REATTORE DI RIFASAMENTO')"
elif sapClass == 'TRASFORMATORE MT/MT':
    hierarchypath = "AND (HIERARCHYPATH='STAZIONE\\TRASFORMAZIONI\\TRASFORMATORE\\MACCHINA TRASFORMATORE MT/MT' OR HIERARCHYPATH='STAZIONE\\MACCHINA TRASFORMATORE MT/MT')"
elif sapClass == 'TRASFORMATORE AT/MT':
    hierarchypath = "AND (HIERARCHYPATH='STAZIONE\\TRASFORMAZIONI\\TRASFORMATORE\\MACCHINA TRASFORMATORE AT/MT' OR HIERARCHYPATH='STAZIONE\\MACCHINA TRASFORMATORE AT/MT')"
elif sapClass == 'TRASFORMATORE DI INTERCONNESSIONE':
    hierarchypath = "AND (HIERARCHYPATH='STAZIONE\\TRASFORMAZIONI\\TRASFORMATORE\\MACCHINA TRASFORM. DI INTERCONNESSIONE' OR HIERARCHYPATH='STAZIONE\\MACCHINA TRASFORM. DI INTERCONNESSIONE')"
elif sapClass == 'TRASFORMATORE AT/BT ISOLATO IN OLIO':
    hierarchypath = "AND (HIERARCHYPATH='STAZIONE\\TRASFORMAZIONI\\TRASFORMATORE AT/BT\\MACCHINA TRASFORMATORE AT/BT IN OLIO' OR HIERARCHYPATH='STAZIONE\\MACCHINA TRASFORMATORE AT/BT IN OLIO')"

# Aggiungere TRASFORMATORE AT/BT ISOLATO IN OLIO

# Ricerca degli asset in base alla classe SAP e (eventualmente) allo hierarchypath
assetSet = sapAsset.getMboSet("$ASSETPOSNREQP", "ASSET", "(siteid, assetnum) in (select siteid, objectnum from X3SAPTOMAXOBJECT xs where xs.sapassetid=:x3sapassetid %s)"%hierarchypath)

countAsset = assetSet.count()

# Gestione del caso in cui viene trovato esattamente un asset
if countAsset == 1:
    asset = assetSet.moveFirst()
    hierarchyPathAsset = asset.getString("CLASSSTRUCTURE.HIERARCHYPATH")
    posnrSet = asset.getMboSet("$X3TRASCODIFICAPSNR", "X3TRASCODIFICAPSNR", "UPPER(:X3FUNZIONEELEMENTO)=UPPER(ELEMENTO) AND HIERARCHYPATH='%s' AND (:X3RIFERIMENTOFASE IS NULL OR UPPER(:X3RIFERIMENTOFASE)=UPPER(RIFERIMENTOFASE))"%hierarchyPathAsset)
    posnrCount = posnrSet.count()
    # Gestione del caso in cui viene trovato esattamente un mapping Posnr
    if posnrCount == 1:
        posnr = posnrSet.moveFirst()
        mappedRawValue = posnr.getString("TRASCODIFICASAP")
    else:
        ruleHandler.addSimpleWarningForRule(mappingRule)
else:
    ruleHandler.addSimpleWarningForRule(mappingRule)