from psdi.server import MXServer

stalliWoSet=mbo.getMboSet("X4STALLIWO")
wonum=mbo.getString("WONUM")
siteid=mbo.getString("SITEID")

childNoTaskSet=mbo.getMboSet("CHILDNOTASK")
if mbo.isModified("STATUS") and mbo.getString("STATUS")=="SCHEDULATO" and not childNoTaskSet.isEmpty():
    for k in range(0,childNoTaskSet.count()):
        childNoTask=childNoTaskSet.getMbo(k)
        if childNoTask.getString("STATUS")=="SCHEDULABILE":
            childNoTask.changeStatus("SCHEDULATO", MXServer.getMXServer().getDate(), "Modificato da Geocall")

if stalliWoSet.isEmpty():
    stalli={}
    childSet=mbo.getMboSet("$ACTMICRO$","WOACTIVITY","parent=:wonum and siteid=:siteid")
    if not childSet.isEmpty():
        for i in range (0,childSet.count()):
            childMbo=childSet.getMbo(i)
            #Prendo 3 livello
            asset4LvlSet=childMbo.getMboSet("ASSET")
            if not asset4LvlSet.isEmpty():
                asset4Lvl=asset4LvlSet.getMbo(0)
                asset3LvlSet=asset4Lvl.getMboSet("PARENT")
                if not asset3LvlSet.isEmpty():
                    asset3Lvl=asset3LvlSet.getMbo(0)
                    sapid3lvl=asset3Lvl.getString("X3SAPID")
                    if sapid3lvl not in stalli:
                        stalli[sapid3lvl]=True
                    #Prendo 2 livello
                    asset2LvlSet=asset3Lvl.getMboSet("PARENT")
                    if not asset2LvlSet.isEmpty():
                        asset2Lvl=asset2LvlSet.getMbo(0)
                        sapid4lvl=asset2Lvl.getString("X3SAPID")
                        if sapid4lvl not in stalli:
                            stalli[sapid4lvl]=True
        for j in stalli:
            newStallo=stalliWoSet.add()
            newStallo.setValue("WONUM",wonum)
            newStallo.setValue("SITEID",siteid)
            newStallo.setValue("SAPID",j)
        stalliWoSet.save()
        stalliWoSet.close()
else:
    stalliWoSet.close()