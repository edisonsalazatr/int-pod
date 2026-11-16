# X3CONTRONCOINDISP
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_TRONCO_INDISP.
# v1.0 - 06/12/2023
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

# prende sapclass
sapClass = mappingRule.getString("SAPCLASS")

# prende la specifica che contiene il tronconum
if sapClass=="CAMPATA":
	assetSpecSet = sapAsset.getMboSet("$X3CONTRONCOINDISP","ASSETSPEC","(siteid, assetnum) in (select o.siteid, o.objectnum from X3SAPTOMAXOBJECT o, ASSET a WHERE a.ASSETNUM = o.OBJECTNUM AND a.SITEID = o.SITEID AND a.X3DADOPPIATERNA = 0 AND o.sapassetid=:x3sapassetid AND o.hierarchypath = 'LINEA\CAMPATA AEREA\CONDUTTORI') and assetattrid='LIN_ELEMENTO_INDISP'")
else:
	assetSpecSet = sapAsset.getMboSet("$X3CONTRONCOINDISP","ASSETSPEC","(siteid, assetnum) in (select siteid, objectnum from X3SAPTOMAXOBJECT where sapassetid=:x3sapassetid and hierarchypath = 'LINEA\TRATTA CAVO\CAVI') and assetattrid='LIN_ELEMENTO_INDISP'")


countAssetSpecSet = assetSpecSet.count()
if countAssetSpecSet == 0 :
	# non è stato trovato l'attributo nelle ASSETSPEC
	ruleHandler.addSimpleWarningForRule(mappingRule)
else:
    assetspec = assetSpecSet.moveFirst()
    mappedRawValue=assetspec.getString("TABLEVALUE")