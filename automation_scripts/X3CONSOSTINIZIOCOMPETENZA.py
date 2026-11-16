# X3CONSOSTINIZIOCOMPETENZA
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_SOST_INIZIO_COMPETENZA.
# Calcola Codice sede tecnica SAP del sostegno o estremo cavo con POSNR minore (escluse derivazioni e asset decommisioned) 
# v1.0 - 12\07\2023
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from psdi.server import MXServer


## recupero il mapping dell'asset di primo livello (linea)
mappingSet = sapAsset.getMboSet("$X3SAPTOMAXOBJECTSOSTINCOMP$", "X3SAPTOMAXOBJECT", "sapassetid=:x3sapassetid and hierarchypath = 'LINEA'")
mapping = mappingSet.getMbo(0)

if mapping == None :
	# non esiste il mapping verso la linea
	ruleHandler.addSimpleWarningForRule(mappingRule, "Linea %s not found"%sapAsset.getString("ROOTSTRNO"))
else:
	# sostegni aerei e estremo cavo della linea non dismesse
	assetRelationSet = mapping.getMboSet("$ASSETLOCRELATION$", "ASSETLOCRELATION", "ASSETLOCRELATIONID IN (SELECT ASSETLOCRELATIONID FROM ASSETLOCRELATION a, ASSET b WHERE a.TARGETASSETNUM = b.ASSETNUM AND a.SOURCEASSETNUM = :OBJECTNUM AND NOT REGEXP_LIKE(a.X3SEQUENCE, '^[A-Za-z]') AND b.CLASSSTRUCTUREID IN (SELECT CLASSSTRUCTUREID FROM CLASSSTRUCTURE c WHERE c.CLASSIFICATIONID IN ('SOSTEGNO AEREO','ESTREMO CAVO')) AND b.status NOT IN (SELECT VALUE FROM SYNONYMDOMAIN WHERE DOMAINID = 'LOCASSETSTATUS' AND MAXVALUE = 'DECOMMISSIONED'))")
	assetRelationSet.setOrderBy("X3SEQUENCE")
	assetRelation = assetRelationSet.getMbo(0)

	if assetRelation == None :
		# non esistono sostegni o estremo Cavo in questa linea
		ruleHandler.addSimpleWarningForRule(mappingRule, "SOSTEGNO AEREO/ESTREMO CAVO not found for LINEA %s"%sapAsset.getString("ROOTSTRNO"))
	else:
		mappedRawValue = assetRelation.getString("X3SAPID")