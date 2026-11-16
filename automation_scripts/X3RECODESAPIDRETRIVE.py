from psdi.server import MXServer
from psdi.mbo import MboConstants


mbo = mbo.getThisMboSet().getMbo(0)
assetnum = mbo.getString("ASSETNUM")
siteid = mbo.getString("SITEID")
sourceassetnum = mbo.getString("OWNERASSET")

assetloc = MXServer.getMXServer().getMboSet("ASSETLOCRELATION", mbo.getUserInfo())
assetloc.setWhere("targetassetnum='"+str(assetnum)+"' and siteid='"+str(siteid)+"' and sourceassetnum='"+str(sourceassetnum)+"'")
if assetloc.count() > 0:
    sapid = assetloc.getMbo(0).getString("X3SAPID")
    lung=len(sapid)
    if lung==16:
        sapidsecondlvl = sapid[-8:]
    elif lung==18:
        sapidsecondlvl = sapid[-10:]
        sapidsecondlvl = sapidsecondlvl[:8]
    if sapidsecondlvl:
        mbo.setValue("NEWX3SAPID", sapidsecondlvl, MboConstants.NOACCESSCHECK)