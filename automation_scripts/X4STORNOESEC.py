from psdi.server import MXServer
wonum = mbo.getString("WONUM")
siteid = mbo.getString("SITEID")
workflownome='X4WOMAIN-H'

if mbo.isModified("X4STORNOGEOCALL") and mbo.getString("X4STORNOGEOCALL")=="OK" and mbo.getMboSet("X4APPRLABOR").isEmpty()  and not mbo.isNull("X4STORNOSAP") :
    mbo.setValue("X4STORNOSAP","S",11L)
if mbo.isModified("X4STORNOGEOCALL") and mbo.getString("X4STORNOGEOCALL")=="KO" and mbo.getMboSet("X4APPRLABOR").isEmpty()  and not mbo.isNull("X4STORNOSAP") :
    mbo.setValue("X4STORNOSAP","KO",11L)
if (mbo.isModified("X4STORNOSAP") and mbo.getString("X4STORNOSAP")=="S") or (mbo.isModified("X4STORNOGEOCALL") and mbo.getString("X4STORNOGEOCALL")=="OK" and not mbo.isNull("X4STORNOSAP") and mbo.getMboSet("X4APPRLABOR").isEmpty()):
    wfSrv = wfInstanceSet=mbo.getMboSet("$EMXWFINSTANCE","WFINSTANCE","processname='"+str(workflownome)+"' and ownertable='WORKORDER' and ownerid=:workorderid and active=1")
    wfInstanceMbo=wfInstanceSet.moveFirst()
    if wfInstanceMbo:
        wfInstanceMbo.stopWorkflow("Stopped via script")
    mbo.getThisMboSet().save()
    mbo.getThisMboSet().reset()
    
    sameWo = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
    sameWo.setWhere("WONUM='%s' and siteid='%s'" % (wonum, siteid))
    sameWo.reset()
    sameWoMbo = sameWo.getMbo(0)
    
    
    
    new_wonum = mbo.getString("WONUM") + "_storno"
    sameWoMbo.setValue("STATUS", "APERTO",11L)
    sameWoMbo.setValue("SCHEDSTART", None)
    sameWoMbo.setValue("SCHEDFINISH", None)
    sameWoMbo.setValue("ACTSTART", None)
    sameWoMbo.setValue("ACTFINISH", None)
    sameWoMbo.setValue("X4FISICO","0")
    sameWoMbo.setValue("X4MODULICAR","0")
    sameWoMbo.setValue("X4MODULICAR","0")
    sameWoMbo.setValue("X4ANAGRAFICAUPDATE","0")
    sameWoMbo.setValueNull("X4ERRSAP")

    figliSet=sameWoMbo.getMboSet("CHILDNOTASK")
    if not figliSet.isEmpty():
        for j in range (0,figliSet.count()):
            figlio=figliSet.getMbo(j)
            figlio.setValue("HISTORYFLAG", "0",11L)
            figlio.setValue("STATUS","APERTO",11L)
            figlio.setValue("X4FISICO","0",11L)
            figliSet.save()
        #figliSet.save()
    figliSet.close()
    '''
    figliSet2=mbo.getMboSet("CHILDNOTASK")
    if not figliSet2.isEmpty():
        for f in range (0,figliSet2.count()):
            figlio2=figliSet2.getMbo(f)
            figlio2.setValue("SCHEDSTART", None,11L)
            figlio2.setValue("SCHEDFINISH", None,11L)
            figlio2.setValue("ACTSTART", None,11L)
            figlio2.setValue("ACTFINISH", None,11L)
            figlio2.setValue("X4FISICO","0",11L)
        figliSet2.save()
    figliSet2.close()
    
     
    where_clause= "wonum='%s' and siteid='%s'" % (mbo.getString("WONUM"), mbo.getString("SITEID"))
    MXServer.getMXServer().lookup("MIC").exportData(
    "X4SENDCANCELODL",
    "X4SENDCANCELODLKAFKA", 
    where_clause,
    mbo.getUserInfo(),
    1
    )
    
    #assignments_set = sameWoMbo.getMboSet("ASSIGNMENT")
    #for i in range(0, assignments_set.count()):
        #assignment = assignments_set.getMbo(i)
        #assignment.setValue("WONUM", new_wonum, 11L)
    '''    
    #assignments_set.deleteAll()
    
    labtrans_set = MXServer.getMXServer().getMboSet("LABTRANS", mbo.getUserInfo())
    labtrans_set.setWhere("REFWO='%s'" % mbo.getString("WONUM"))
    #labtrans_set = mbo.getMboSet("LABTRANS")
    labtrans_set.reset()
    if not labtrans_set.isEmpty():
        for i in range(0, labtrans_set.count()):
            labtrans = labtrans_set.getMbo(i)
            refwo=labtrans.getString("REFWO")
            labtrans.setValue("X4STORNO", "T", 11L)
            #labtrans.setValue("REFWO", refwo+"_S", 11L)
    
    #wo.setValue("WONUM", new_wonum, 11L)
    labtrans_set.save()
    #assignments_set.close()
    
    
    sameWo.save()
    #sameWo.close()
    labtrans_set.close()
    
    '''
    labtrans_sap=mbo.getMboSet("$LABTRANSSAP$","LABTRANS","refwo=:WONUM and siteid=:SITEID and X4CHECKSAP='OK'")
    if labtrans_sap.isEmpty():
        PublishChannelCache.getInstance().getPublishChannel("X4STORNOGEOCALL").publish(mbo, True);
    #wfSrv = MXServer.getMXServer().lookup('WORKFLOW')
    #wfSrv.initiateWorkflow(workflownome, mbo)