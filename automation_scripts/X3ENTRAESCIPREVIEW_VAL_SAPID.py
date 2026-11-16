from psdi.mbo import MboConstants
from java.util.regex import Pattern
from java.util import HashMap
from com.terna.common.log import Logger

def existsRename(campataNew):
	for mboPreview in arrayPreview:
		if mboPreview.getString("SAPID")[0:-2] == campataNew.getString("SAPID") and mboPreview.getString("OPERATION")=="RENAME":
			return True
	return False

# Init
logger = Logger.getLogger("X3_VAL_SAPID")
mboSetPreview = mbo.getThisMboSet()
arrayPreview = []
# Inizializzo array preview
preview = mboSetPreview.moveFirst()
while preview:
	arrayPreview.append(preview)
	preview = mboSetPreview.moveNext()
integrationService = service.invokeScript("X3INTEGRATIONSERVICE").get("IntegrationService")(service,mbo.getOwner().getOwner().getString("SITEID"),mbo.getThisMboSet().getUserInfo())
#--------------------------------------------------------

# Validazione formato SAPID
# Validazione SAPID oggetti movimentati
if mbo.getString("OPERATION")=="MOVE" and not integrationService.validateSapId(mbo.getOwner().getOwner(),mbo.getMboSet("ASSET").getMbo(0),mbo.getString("SAPID")):
	service.error("x3terna","ErrorMsg",["Formato del SAPID %s non valido."%mbo.getString("SAPID")])
# Validazione sapid campata new
elif mbo.getString("OPERATION")=="NEW":
	regexCampata = "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}$"
	pattern = Pattern.compile(regexCampata)
	matcher = pattern.matcher(mbo.getString("SAPID"))
	if not matcher.find():
		service.error("x3terna","ErrorMsg",["Formato del SAPID %s non valido."%mbo.getString("SAPID")])
# Validazione codice linea
if mbo.getString("TMPSAPID") != "" and mbo.getString("SAPID")[0:8] != mbo.getString("TMPSAPID")[0:8]:
	service.error("x3terna","ErrorMsg",["Non è possibile modificare il codice della linea".decode("utf-8")])
#--------------------------------------------------------
# Validazione univocità SAPID
errorFlag=False
for preview in arrayPreview:
	if preview.getString("SAPID") == mbo.getString("SAPID") and preview.getString("TARGETASSETNUM") != mbo.getString("TARGETASSETNUM"):
		errorFlag=True
		break
# Verifico che non esista lo stesso SAPID in anagrafica
if not errorFlag:
	assetLocRelationSet=None
	#logger.info("CLASSIFICATIONID: {0}",mbo.getString("CLASSIFICATIONID"))
	if mbo.getString("CLASSIFICATIONID") in ("ESTREMO CAVO","CAMERA GIUNTI","SOSTEGNO AEREO"):
		assetLocRelationSet = mbo.getMboSet("$checkSapId_6324$","ASSETLOCRELATION","substr(:SAPID,1,16)=substr(X3SAPID,1,16)")
	else:	
		assetLocRelationSet = mbo.getMboSet("$checkSapId_6324$","ASSETLOCRELATION",":SAPID=X3SAPID")

	logger.info("Where clause {0}", assetLocRelationSet.getRelationship())
	assetLocRelationSet.reset()
	errorFlag=not assetLocRelationSet.isEmpty()
# Verifico che non esista lo stesso SAPID nel connettore
if not errorFlag:
	errorFlag= not integrationService.isSapIdAvailable(mbo.getString("SAPID"))
# Aggiorno ERRORFLAG
mbo.setValue("ERRORFLAG",errorFlag)
# Se cambio SAPID vado a verificare se ha impatto su altro
for preview in arrayPreview:
	if preview.getString("SAPID") == mbo.getString("TMPSAPID") and mbo.getString("SAPID") != preview.getString("SAPID"):
		vars = HashMap()
		vars.put("mbo",preview)
		vars.put("service",service)
		service.invokeScript("X3ENTRAESCIPREVIEW_VAL_SAPID",vars)
		break
#----------------------------------------------------------
# Rendo editabile la campata in caso in cui esista un sostegno con operation "RENAME"
if mbo.getString("OPERATION")=="RENAME":
	for mboPreview in arrayPreview:
		if mboPreview.getString("SAPID")==mbo.getString("SAPID")[0:-2]:
			mboPreview.setFieldFlag("SAPID",MboConstants.READONLY,False)
			break
# Setto READONLY i sostegni (tranne il primo)
if (mbo.getString("OPERATION") != "MOVE" and mbo.getString("TARGETASSETNUM")!=arrayPreview[0].getString("TARGETASSETNUM"))  or (mbo.getString("ASSET.CLASSSTRUCTURE.CLASSIFICATIONID") in ("SOSTEGNO AEREO","ESTREMO CAVO") and mbo.getString("TARGETASSETNUM")!=arrayPreview[0].getString("TARGETASSETNUM")):
	mbo.setFieldFlag("SAPID", MboConstants.READONLY, True)

# Aggiorno SAPID del Sostegno o Estremo cavo uguale a quello della campata o tratta cavo ad esso legato
oldValue = 	mbo.getString("TMPSAPID")
newValue = ""
mboPreview = mboSetPreview.moveFirst()
for mboPreview in arrayPreview:
	if (mboPreview.getString("SAPID") == oldValue+"-S" or mboPreview.getString("SAPID") == oldValue+"-H" or mboPreview.getString("SAPID") == oldValue+"-R") and mboPreview.getString("SEQUENCE")>mbo.getString("SEQUENCE"):
		mboPreview.setFieldFlag("SAPID", MboConstants.READONLY, False)
		trailing = "-"+mboPreview.getString("SAPID")[-1]
		mboPreview.setValue("SAPID",mbo.getString("SAPID")+trailing)
		mboPreview.setValue("TMPSAPID",mbo.getString("SAPID")+trailing)
		mboPreview.setFieldFlag("SAPID", MboConstants.READONLY, True)
		wcs = service.webclientsession()
		if wcs is not None: 
			bean = wcs.getDataBean("x3entraesci_preview_table") 
			if bean is not None: 
				bean.fireStructureChangedEvent() 
				bean.refreshTable()
		break

mbo.setValue("TMPSAPID",mbo.getString("SAPID"))