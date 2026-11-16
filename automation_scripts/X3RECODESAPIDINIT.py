from psdi.server import MXServer
from psdi.mbo import MboConstants

owner = mbo.getOwner()
assetnum = owner.getString("ASSETNUM")
siteid = owner.getString("SITEID")

mbo.getThisMboSet().getMbo(0).setValue("OWNERASSET",assetnum)
mbo.getThisMboSet().getMbo(0).setValue("SITEID",siteid)


appName = mbo.getThisMboSet().getParentApp()
if appName=="X3STAZIONI":
    mbo.getThisMboSet().getMbo(0).setValue("OLDSAPID",owner.getString("X3SAPID"))
    mbo.getThisMboSet().getMbo(0).setValue("NEWX3SAPID",owner.getString("X3SAPID")[:-2]+"XX")