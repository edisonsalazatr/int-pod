from psdi.mbo import MboConstants
from psdi.server import MXServer

childNoTaskSet=mbo.getMboSet("CHILDNOTASK")
wonum=mbo.getString("WONUM")
siteid=mbo.getString("SITEID")
macro=mbo.getString("WOJP2")
if mbo.getString("STATUS")=="APERTO" or mbo.getString("STATUS")=="DA VALIDARE":
    if childNoTaskSet.isEmpty():
        mbo.removeWorkPlan()
        #mbo.setFieldFlag("JPNUM", MboConstants.REQUIRED, False)
        #mbo.setValueNull("JPNUM",11L)
        mbo.getThisMboSet().save()
    else:
        def yes():
            for i in range (0,childNoTaskSet.count()):
                childNoTask=childNoTaskSet.getMbo(i)
                childNoTask.changeWorkOrderParent(None)
            childNoTaskSet.save()
            childNoTaskSet.close()
            #mbo.getThisMboSet().save()
            mbo.getMboSet("$ANCESTORSF$","WOANCESTOR","ancestor=:wonum and wonum!=:wonum").deleteAll()
            #mbo.setFieldFlag("JPNUM", MboConstants.REQUIRED, False)
            WoSet = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
            WoSet.setWhere("wonum='"+str(wonum)+"' and siteid='"+str(siteid)+"'") 
            WoSet.reset()
            wo=WoSet.getMbo(0)
            wo.setRemoveWorkPlanFlag(False)
            wo.removeWorkPlan()
            WoSet.save()
            wo.setValue("WOJP2",macro)
            WoSet.save()
        def no():
            pass
        def dflt():
        	service.yncerror("x3","GenericYesNo",["Esistono figli. Si vogliono eliminare anche i figli? (Nel caso si clicchi il NO, il PdL non sara' rimosso.)"])
        	
        cases = {service.YNC_NULL: dflt,service.YNC_YES: yes,service.YNC_NO: no}
        if interactive:
        	x = service.yncuserinput()
        	cases[x]()
else:
    service.error("x3terna","ErrorMsg", ["Stato non ammissibile."] )