# X3CONTRONCOISP
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_TRONCO_ISP.
# Calcola Codice sede tecnica SAP del Tronco di Ispezione. 
# v1.0 - 17/07/2023
# v1.1 - 20/09/2023
# v1.2 - 04/10/2023 - Gestione ammazzettamento
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

# prende sapclass
sapClass = mappingRule.getString("SAPCLASS")

# prende la specifica che contiene il tronconum
if sapClass=="CAMPATA":
	tronchiSpecSet = sapAsset.getMboSet("$TRONCHISPEC","ASSETSPEC","(SITEID,ASSETNUM) IN (SELECT o.SITEID, o.OBJECTNUM FROM X3SAPTOMAXOBJECT o, ASSET a WHERE a.assetnum = o.objectnum and a.siteid = o.siteid and a.x3dadoppiaterna = 0 and o.SAPASSETID = :X3SAPASSETID AND o.HIERARCHYPATH = 'LINEA\CAMPATA AEREA\CONDUTTORI') AND ASSETATTRID = 'LIN_TRONCO_ISPEZIONE'")
else:
	tronchiSpecSet = sapAsset.getMboSet("$TRONCHISPEC","ASSETSPEC","(SITEID,ASSETNUM) IN (SELECT SITEID, OBJECTNUM FROM X3SAPTOMAXOBJECT WHERE SAPASSETID = :X3SAPASSETID AND HIERARCHYPATH = 'LINEA\TRATTA CAVO\CAVI') AND ASSETATTRID = 'LIN_TRONCO_ISPEZIONE'")

countTronchi = tronchiSpecSet.count()
if countTronchi == 0 :
	# non è stato trovato l'attributo nelle ASSETSPEC
	ruleHandler.addSimpleWarningForRule(mappingRule)
else:
	troncoSpec = tronchiSpecSet.moveFirst()
	tronchiSet=troncoSpec.getMboSet("$TRONCO","X3TRONCHI",":TABLEVALUE=TRONCONUM")
	tronchiCount = tronchiSet.count()
	if tronchiCount > 0:
		troncoMbo = tronchiSet.moveFirst()
		mappedRawValue = troncoMbo.getString("SAPID")