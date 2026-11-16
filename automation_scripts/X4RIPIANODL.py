from psdi.mbo import MboConstants
from psdi.server import MXServer

wo = mbo
wo_status = mbo.getString("STATUS")
wonum = mbo.getString("WONUM")
siteid = mbo.getString("SITEID")
workflownome="X4WOMAIN-H"
existsLab=mbo.getMboSet("LABTRANS")

if not mbo.isNull("PARENT"):
        service.error("x3terna", "ErrorMsg", ["Impossibile ripianificare un OdL figlio. Si prega di procedere con la ripianificazione dell'OdL padre."])

if wo_status in("SCHEDULABILE","SCHEDULATO","ESEGUIBILE") and existsLab.isEmpty():
    def yes():
        sameWo = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
        sameWo.setWhere("WONUM='%s' and siteid='%s'" % (wonum, siteid))
        sameWo.reset()
        
        sameWoMbo = sameWo.getMbo(0)
        sameWoMbo.setValue("SCHEDSTART", None)
        sameWoMbo.setValue("SCHEDFINISH", None)
        sameWoMbo.setValue("ACTSTART", None)
        sameWoMbo.setValue("ACTFINISH", None)
        #sameWoMbo.setValue("X4STORNOSAP", "T")
        sameWoMbo.setValue("X4MODULICAR","0")
        sameWoMbo.setValue("X4MODULICAR","0")
        sameWoMbo.setValue("X4ANAGRAFICAUPDATE","0")
        
        
        if wo_status in("SCHEDULABILE","SCHEDULATO"):
            where_clause= "wonum='%s' and siteid='%s'" % (mbo.getString("WONUM"), mbo.getString("SITEID"))
            '''MXServer.getMXServer().lookup("MIC").exportData(
            "X4SENDCANCELODL",
            "X4SENDCANCELODLKAFKA", 
            where_clause,
            mbo.getUserInfo(),
            1
            )
            
            assignments_set = sameWoMbo.getMboSet("ASSIGNMENT")
            for i in range(0, assignments_set.count()):
                assignment = assignments_set.getMbo(i)
                assignment.setValue("WONUM", new_wonum, 11L)
            '''     
        sameWoMbo.changeStatus("APERTO",MXServer.getMXServer().getDate(),"Ripianificazione OdL")
        sameWoMbo.setValue("WOLO1", "CANCEL")
        wofigli=sameWoMbo.getMboSet("CHILDNOTASK")
        if not wofigli.isEmpty():
            for l in range (0,wofigli.count()):
                wofiglioMbo=wofigli.getMbo(l)
                wofiglioMbo.changeStatus("APERTO",MXServer.getMXServer().getDate(),"Ripianificazione OdL")
                
    
        sameWo.save()
        sameWo.close()
        wfInstanceSet=mbo.getMboSet("$EMXWFINSTANCE","WFINSTANCE","processname='"+str(workflownome)+"' and ownertable='WORKORDER' and ownerid=:workorderid and active=1")
        wfInstanceSet.reset()
        wfInstanceMbo=wfInstanceSet.moveFirst()
        if wfInstanceMbo:
            wfInstanceMbo.stopWorkflow("Stopped via script, ripianificazione")
            wfInstanceSet.save()
        
        wfSrv = MXServer.getMXServer().lookup('WORKFLOW')
        wfSrv.initiateWorkflow(workflownome, sameWoMbo)
    def no():
        pass

    def dflt():
    	service.yncerror("x3","GenericYesNo",["Attenzione, l ordine verra ripianificato e quindi riportato in stato Aperto. Procedere?"])
    	
    cases = {service.YNC_NULL: dflt,service.YNC_YES: yes,service.YNC_NO: no}
    if interactive:
    	x = service.yncuserinput()
    	cases[x]()
    
else:
    service.error("x3terna", "ErrorMsg", ["OdL non ripianificabile in quanto lo stato diverso da Eseguibile, Schedulabile o Schedulato oppure sono presenti dei consuntivi."])