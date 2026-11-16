from psdi.mbo import MboConstants
from psdi.server import MXServer


WONUM = mbo.getOwner().getString("WONUM")
if mbo.getOwner().getString("STATUS") != "APERTO" and mbo.getOwner().getString("STATUS") != "DA VALIDARE":
    service.error("x3terna","ErrorMsg",["Stato dell'ordine di lavoro padre non ammissibile."])
X4COIN = mbo.getOwner().getString("X4COIN")
X4TIPOSPESA = mbo.getOwner().getString("X4TIPOSPESA")
X4NATURALAVORO = mbo.getOwner().getString("X4NATURALAVORO")
X4OUTSOURCING = mbo.getOwner().getInt("X4OUTSOURCING")

# dialog selected assets
app = service.webclientsession().getCurrentApp()
if app is None:
    service.error("AppError: ", "L'oggetto 'app' non è inizializzato")
    
if mbo.getOwner().getBoolean("HASCHILDREN")==False:
    efforto=0
    efforti=0
    effortq=0
    fisicoplan=0
    ttrasf=0
else:
    efforto=mbo.getOwner().getDouble("X4EFFORTO")
    efforti=mbo.getOwner().getDouble("X4EFFORTI")
    effortq=mbo.getOwner().getDouble("X4EFFORTQ")
    fisicoplan=mbo.getOwner().getDouble("X4FISICOPLAN")
    ttrasf=mbo.getOwner().getDouble("X4TTRASF")

woDialogSet = app.getDataBean("X4RELATEORDERS")

if woDialogSet is None:
    service.error("DialogError: ", "Dialog non esistente")

selectedWOs = woDialogSet.getSelection()
numSelWO  = selectedWOs.size()

for i in range(0,numSelWO):
    selectedWOwarn = selectedWOs.get(i)
    if not selectedWOwarn.isNull("X4ODM") or not selectedWOwarn.isNull("X4NTK"):
        service.error("x3terna","ErrorMsg", ["Esistono OdL selezionati con il campo OdM o NTK popolato. Impossibile procedere con l'aggregazione."] )

for i in range(0,numSelWO):
    selectedWO = selectedWOs.get(i)
    workoderNew2Set=MXServer.getMXServer().getMboSet("WORKORDER",MXServer.getMXServer().getSystemUserInfo())
    workoderNew2Set.setWhere("wonum='"+selectedWO.getString("WONUM")+"' and siteid='"+selectedWO.getString("SITEID")+"'")
    workoderNew2Set.reset()
    workoderNew2=workoderNew2Set.getMbo(0)
    workoderNew2.setValue("PARENT", WONUM, 11L)
    workoderNew2.setValue("X4COIN", X4COIN, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    workoderNew2.setValue("X4TIPOSPESA", X4TIPOSPESA, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    workoderNew2.setValue("X4OUTSOURCING", X4OUTSOURCING, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    workoderNew2.setValue("X4NATURALAVORO", X4NATURALAVORO, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    workoderNew2.setValue("X4NORDINEINTERNO",mbo.getOwner().getString("X4NORDINEINTERNO"),11L)
    if workoderNew2.getString("OWNERGROUP")==mbo.getOwner().getString("OWNERGROUP"):
        workoderNew2.setValue("X4WBE",mbo.getOwner().getString("X4WBE"),11L)
        workoderNew2.setValue("X4WBS",mbo.getOwner().getString("X4WBS"),11L)
        workoderNew2.setValue("X4ODM",mbo.getOwner().getString("X4ODM"),11L)
        workoderNew2.setValue("X4NTK",mbo.getOwner().getString("X4NTK"),11L)
        workoderNew2.setValue("X4NOPERAZIONE",mbo.getOwner().getString("X4NOPERAZIONE"),11L)
        workoderNew2.setValue("X4TIPOORDINE",mbo.getOwner().getString("X4TIPOORDINE"),11L)
        workoderNew2.setValue("X4TIPOOI",mbo.getOwner().getString("X4TIPOOI"),11L)
    efforto=efforto+workoderNew2.getDouble("X4EFFORTO")
    effortq=effortq+workoderNew2.getDouble("X4EFFORTQ")
    efforti=efforti+workoderNew2.getDouble("X4EFFORTI")
    fisicoplan=fisicoplan+workoderNew2.getDouble("X4FISICOPLAN")
    ttrasf=ttrasf+workoderNew2.getDouble("X4TTRASF")
    sapidFiglio=workoderNew2.getString("X4SAPIDASSET")
    if sapidFiglio[:7]!=mbo.getString("X4SAPIDASSET")[:7]:
	    assetFiglio=workoderNew2.getString("ASSETNUM")
	    sapidAltralineaSet = MXServer.getMXServer().getMboSet("ASSETLOCRELATION", mbo.getUserInfo())
	    sapidAltralineaSet.setWhere("(targetassetnum='"+str(assetFiglio)+"' or targetassetnum in (select ancestor from asset where assetnum='"+str(assetFiglio)+"')) and x3sapid like '"+str(mbo.getString("X4SAPIDASSET")[:7])+"%'") 
	    sapidAltralineaSet.reset()
	    if not sapidAltralineaSet.isEmpty():
	        sapidAltralinea=sapidAltralineaSet.getMbo(0)
	        newSapid=sapidAltralinea.getString("X3SAPID")
	        workoderNew2.setValue("X4SAPIDASSET",newSapid)
    workoderNew2Set.save()
mbo.getThisMboSet().save(11L)
workoderNewSet=MXServer.getMXServer().getMboSet("WORKORDER",MXServer.getMXServer().getSystemUserInfo())
workoderNewSet.setWhere("wonum='"+mbo.getOwner().getString("WONUM")+"' and siteid='"+mbo.getOwner().getString("SITEID")+"'")
workoderNew=workoderNewSet.getMbo(0)
#workoderNew=mbo.getOwner()
workoderNew.setValue("X4AGGREGATI","1",11L)
workoderNew.setValue("X4EFFORTO",efforto,11L)
workoderNew.setValue("X4EFFORTI",efforti,11L)
workoderNew.setValue("X4EFFORTQ",effortq,11L)
workoderNew.setValue("X4FISICOPLAN",fisicoplan,11L)
workoderNew.setValue("X4TTRASF",ttrasf,11L)
workoderNewSet.save()
#mbo.getThisMboSet().save()
#mbo.getThisMboSet().save(11L)
# Rinfresca la tabella dei dati
mbo.getOwner().getThisMboSet().reset()
		
# Rinfresca la tabella dei dati
#woDialogSet.refreshTable()
service.closeDialog()

errorgroup = "X4ODLAGG"
errorkey = "X4ODLAGG"