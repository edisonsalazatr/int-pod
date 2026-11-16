from psdi.server import MXServer
from psdi.mbo import MboConstants

def beforeMboData(ctx):
    if(ctx.getProcessTable()=="WORKORDER"):
        status=ctx.getData().getCurrentData("STATUS")
        x4dss=ctx.getData().getCurrentData("X4DSS")
        mbo = ctx.getMbo();
        if status=="SCHEDULABILE" and mbo.getString("STATUS")!="SCHEDULABILE":
            ctx.getData().setCurrentData("STATUS", mbo.getString("STATUS"))
        if status=="INVIATO" and mbo.getString("STATUS")=="AVVIATO":
            ctx.getData().setCurrentData("STATUS","AVVIATO")
        if status and mbo.getString("STATUS")!=status and (status=="INVIATO" and mbo.getString("STATUS")!="AVVIATO") and mbo.getString("STATUS")!="ESEGUITO PARZ." and mbo.getString("STATUS")!="ESEGUITO":
            if status != "NON ESEGUITO" and (status=="INVIATO" and (mbo.getString("STATUS")=="SCHEDULATO" or mbo.getString("STATUS")=="SCHEDULABILE") and mbo.isNull("PARENT") and mbo.getBoolean("HASCHILDREN")==False):
                mbo.changeStatus("INVIATO", MXServer.getMXServer().getDate(), "Modificato da Geocall");
        if status and mbo.getString("STATUS")!=status and (status=="INVIATO" and mbo.getString("STATUS")!="AVVIATO") and mbo.getString("STATUS")=="ESEGUITO PARZ.":
            ctx.getData().setCurrentData("STATUS","ESEGUITO PARZ.")
        if status and mbo.getString("STATUS")!=status and (status=="AVVIATO" and mbo.getString("STATUS")!="AVVIATO") and mbo.getString("STATUS")=="ESEGUITO PARZ.":
            ctx.getData().setCurrentData("STATUS","ESEGUITO PARZ.")
        if status=="INVIATO" and (mbo.getString("STATUS")=="SCHEDULATO" or mbo.getString("STATUS")=="INOLTRATO"):
            mbo.changeStatus(status, MXServer.getMXServer().getDate(), "Modificato da Geocall");
            woFigli=mbo.getMboSet("CHILDNOTASK")
            if not woFigli.isEmpty():
                for i in range (0,woFigli.count()):
                    woFiglio=woFigli.getMbo(i)
                    if woFiglio.getString("STATUS")=="SCHEDULATO":
                        woFiglio.changeStatus("INOLTRATO", MXServer.getMXServer().getDate(), "Modificato da Geocall");
                        
        if status=="INOLTRATO" and status!=mbo.getString("STATUS") and not mbo.isNull("PARENT") and mbo.getString("STATUS")=="SCHEDULATO":
            mbo.changeStatus(status, MXServer.getMXServer().getDate(), "Modificato da Geocall");
        if status=="INOLTRATO" and (mbo.getString("STATUS")=="AVVIATO" or mbo.getString("STATUS")=="ESEGUITO PARZ." or mbo.getString("STATUS")=="ESEGUITO"):
            ctx.getData().setCurrentData("STATUS",mbo.getString("STATUS"))

        ''' elif status=="INOLTRATO" and status!=mbo.getString("STATUS") and mbo.isNull("PARENT") and mbo.getBoolean("HASCHILDREN")==False and mbo.getString("STATUS")!="INVIATO":
            mbo.changeStatus("INVIATO", MXServer.getMXServer().getDate(), "Modificato da Geocall");'''
        if status=="SCHEDULATO" and mbo.getString("STATUS")=="SCHEDULABILE":
            mbo.changeStatus("SCHEDULATO", MXServer.getMXServer().getDate(), "Modificato da Geocall");
            woFigli=mbo.getMboSet("CHILDNOTASK")
            if not woFigli.isEmpty():
                for i in range (0,woFigli.count()):
                    woFiglio=woFigli.getMbo(i)
                    if woFiglio.getString("STATUS")=="SCHEDULABILE":
                        woFiglio.changeStatus("SCHEDULATO", MXServer.getMXServer().getDate(), "Modificato da Geocall");
        if status=="AVVIATO" and mbo.getString("STATUS")!="ESEGUITO PARZ." and mbo.getString("STATUS")!="ESEGUITO" and mbo.getString("STATUS")!="AVVIATO":
            mbo.changeStatus(status, MXServer.getMXServer().getDate(), "Modificato da Geocall");
        elif status=="AVVIATO" and mbo.getString("STATUS")=="ESEGUITO PARZ.": 
            ctx.getData().setCurrentData("STATUS","ESEGUITO PARZ.")
        elif status=="AVVIATO" and mbo.getString("STATUS")=="ESEGUITO": 
            ctx.getData().setCurrentData("STATUS","ESEGUITO")
        if status=="INOLTRATO" and mbo.getString("STATUS")=="ESEGUITO":
            ctx.getData().setCurrentData("STATUS","ESEGUITO")
        if status=="SCHEDULATO" and mbo.getString("STATUS")=="ESEGUITO PARZ.":
            ctx.getData().setCurrentData("STATUS","ESEGUITO PARZ.")
        if status=="ESEGUITO PARZ." and mbo.getString("STATUS")!="ESEGUITO":
            mbo.changeStatus(status, MXServer.getMXServer().getDate(), "Modificato da Geocall");
        if status=="ESEGUITO PARZ." and mbo.getString("STATUS")=="ESEGUITO":
            ctx.getData().setCurrentData("STATUS","ESEGUITO")
          
        if status=="NON ESEGUITO" and mbo.getString("STATUS")!="ESEGUITO" and x4dss is None:
            mbo.changeStatus(status, MXServer.getMXServer().getDate(), "Modificato da Geocall");
        storno=ctx.getData().getCurrentData("X4STORNOSAP")
        errstorno=ctx.getData().getCurrentData("X4ERRSAP")
        stornogeo=ctx.getData().getCurrentData("X4STORNOGEOCALL")
        if storno:
            mbo.setValue("X4STORNOSAP",storno,11L)
        if errstorno:
            mbo.setValue("X4ERRSAP",errstorno,11L)
        if stornogeo:
            mbo.setValue("X4STORNOGEOCALL",stornogeo,11L)
            if stornogeo=="KO":
                ctx.getData().setCurrentData("X4STORNOSAP","KO")
        x4respasset=ctx.getData().getCurrentData("X4RESPASSET")
        if x4respasset:
            mbo.setValue("X4RESPASSET",x4respasset,11L)
            
    if(ctx.getProcessTable()=="WOACTIVITY"):
        parent=ctx.getData().getCurrentData("PARENT")
        asset=ctx.getData().getCurrentData("ASSETNUM")
        mbo = ctx.getMbo();
        woset=mbo.getThisMboSet()
        woset.setFlag(MboConstants.NOACCESSCHECK, True)
        mbo.setValue("ASSETNUM",asset,9L)
        if parent:
            mbo.setValue("PARENT",parent,9L)