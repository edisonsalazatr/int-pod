# X3CONCAMPTERASS
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_CAMP_TER_ASS.
# I casi gestiti sono:
#   1 - campata in doppia terna: ritorna STRNO della campata associata
#   2 - campata con conduttori ammazzettati: ritorna STRNO della campata stessa
#   3 - negli altri casi (campata singola): ritorna ""
# 
# v1.0 - 07/07/2023
# v1.1 - 20/09/2023
# v1.2 - 03/12/2024
# v1.3 - 10/12/2024 aggiunta gestione campata fittizia in doppia terna
# v1.4 - 23/12/2024 aggiunta gestione campata fittizia ammazzettata
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

# gestione campata fittizia
campataStrno = sapAsset.getString("strno")
if sapAsset.getBoolean("dummy"):
    mappedRawValue = ""
    whereClause = "assetlocrelationid in (select b.assetlocrelationid from assetlocrelation a, assetlocrelation b where a.siteid = b.siteid and a.targetassetnum = b.targetassetnum and a.x3sapid = :strno || '-S' and b.x3sapid <> :strno || '-S')"
    # recupera set sostegno in doppia terna
    assetLocSet = sapAsset.getMboSet("$camp$", "ASSETLOCRELATION", whereClause)
    assetLoc = assetLocSet.getMbo(0)
    # se presente sostegno in doppia terna recupera STRNO campata associata
    if assetLoc != None:
        sapId = assetLoc.getString("x3sapid")
        mappedRawValue = sapId[:-2]
    else:
        # il sostegno non è in doppia terna verifico la presenza di ammazzettamento
        attacchiSet = sapAsset.getMboSet("$ATTACCHI", "ASSET", """x3dadoppiaterna = :yes and 
        (siteid, parent) in (select siteid, targetassetnum from assetlocrelation where x3sapid = :strno || '-S') and 
        classstructureid in (select classstructureid from classstructure where classificationid in ('ATTACCHI CONDUTTORE'))""")
        if not attacchiSet.isEmpty():
            # ammazzettamento presente
            mappedRawValue = campataStrno
else:    
    # prende le relazioni verso le campate
    campataRelSet = sapAsset.getMboSet("$CAMPASSOC", "ASSETLOCRELATION", """(siteid, targetassetnum) in (select siteid, objectnum 
    from x3saptomaxobject where sapassetid = :x3sapassetid and hierarchypath = 'LINEA\CAMPATA AEREA')""")
    numRelations = campataRelSet.count()
    if numRelations == 0 or numRelations > 2:
        # attese una o al massimo due relazioni
        ruleHandler.addSimpleWarningForRule(mappingRule)
        mappedRawValue = ""
    elif numRelations == 2:
        # la campata è in doppia terna 
        sapIdCampata1 = campataRelSet.getMbo(0).getString("x3sapid")
        sapIdCampata2 = campataRelSet.getMbo(1).getString("x3sapid")
        # imposta il SAP id della campata associata 
        mappedRawValue = sapIdCampata1 if sapIdCampata1 != campataStrno else sapIdCampata2
    else:
        # campata singola (verifica se è presente un ammazzettamento)
        campata = campataRelSet.getMbo(0)
        conduttoriSet = campata.getMboSet("$CONDUTTORI", "ASSET", """x3dadoppiaterna = :yes and siteid = :siteid and parent = :targetassetnum and classstructureid in (select classstructureid from classstructure where classificationid in ('CONDUTTORI'))""")
        if not conduttoriSet.isEmpty():
            # esiste un aggregatore conduttori ammazzettato
            mappedRawValue = campataStrno
        else:    
            # non è presente l'ammazzettamento
            mappedRawValue = ""