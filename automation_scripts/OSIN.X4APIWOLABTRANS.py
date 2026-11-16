from psdi.server import MXServer

def beforeMboData(ctx):
    if(ctx.getProcessTable()=="WORKORDER"):
        status=ctx.getData().getCurrentData("STATUS")
        mbo = ctx.getMbo(); 
        if not mbo.isNull("PARENT"):
            ctx.error("x3terna", "ErrorMsg", ["Impossibile caricare consuntivi su odl figlio."])