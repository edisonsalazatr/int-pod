from psdi.mbo import MboConstants
from psdi.server import MXServer
from psdi.iface.mic import PublishChannelCache

persongroupSet1 = MXServer.getMXServer().getMboSet("PERSONGROUPTEAM", mbo.getUserInfo())
persongroupSet1.setWhere("RESPPARTY='"+user+"' and persongroup in(select persongroup from persongroup where x4tipudb in ('AM'))")
persongroupSet1.reset()

laborStor=mbo.getString("OWNERGROUP")
checkPSet=MXServer.getMXServer().getMboSet("PERSONGROUPTEAM", mbo.getUserInfo())
checkPSet.setWhere("persongroup='"+laborStor+"' and persongroup in(select persongroup from persongroupteam where respparty='"+user+"')")
checkPSet.reset()

wfassignmentCheck=mbo.getMboSet("$WFASSCH$","WFASSIGNMENT","EXISTS (select assigncode from wfassignment where ownerid=:WORKORDERID and assignstatus='ACTIVE' AND assigncode='"+user+"') and exists (select 1 from wfassignment where ownerid=:WORKORDERID and assignstatus='ACTIVE')")
        
if persongroupSet1.isEmpty() and checkPSet.isEmpty() and wfassignmentCheck.isEmpty():
    service.error("x3terna", "ErrorMsg", ["Non si dispongono dei permessi necessari per procedere con lo storno."])
    
#wo = mbo
wo_status = mbo.getString("STATUS")
tipospesa = mbo.getString("X4TIPOSPESA")
if tipospesa!="OPEX":
    service.error("x3terna", "ErrorMsg", ["Impossibile effettuare storno totale. Verificare che la tipologia spesa sia OPEX."])

if mbo.getString("x4scenariotronchimig")=="1" or mbo.getString("x4scenariotronchimig")=="2":
    service.error("x3terna", "ErrorMsg", ["Impossibile effettuare storno totale."])

X4ODM = mbo.getString("X4ODM")
STATOSISTEMASET = mbo.getMboSet("$X4ODM$","X4ODM","IDODM = :X4ODM")
if not STATOSISTEMASET.isEmpty():
    STATOSISTEMA=STATOSISTEMASET.getMbo(0).getString("STATOSISTEMA")
    if STATOSISTEMA == "CHIU":
        service.error("x3terna", "ErrorMsg", ["Non e' possibile proseguire con lo storno. Lo stato dell'ODM e' Chiuso."])
    
wonum = mbo.getString("WONUM")
siteid = mbo.getString("SITEID")
workflownome='X4WOMAIN-H'
'''
difforSet=mbo.getMboSet("$DIFFAPERT$","X4DIFFORSTAT","status='VALIDATA' AND x4difforstatid in (select x4difforstatid from x4diffor_wo where wonum=:wonum)")
if not difforSet.isEmpty():
    service.error("x3terna", "ErrorMsg", ["Impossibile stornare. Sono state validate delle anomalie registrate nell'ordine."])
'''

odlGeoSet=mbo.getMboSet("$WPGEO$","WPLABOR","(wonum in (select wonum from workorder where istask=0 and parent=:wonum OR wonum=:wonum)) AND SITEID=:SITEID and x4titris in (select persongroup from persongroup where x4tipudb='UI')")

if wo_status == "CHIUSO":
    mbo.setValue("HISTORYFLAG", "0",11L)
    mbo.setValue("X4STORNOSAP","T",11L)
else:
    if mbo.getString("X4STORNOSAP")=="T":
        service.error("x3terna", "ErrorMsg", ["Impossibile stornare. Storno in corso."])
    elif not odlGeoSet.isEmpty() or not mbo.getMboSet("X4APPRLABOR").isEmpty():
        if mbo.getMboSet("X4APPRLABOR").isEmpty():
            mbo.setValue("X4STORNOSAP","T")
            PublishChannelCache.getInstance().getPublishChannel("X4STORNOGEOCALL").publish(mbo, True);
        else:
            mbo.setValue("X4STORNOSAP","T",11L)
    elif mbo.isNull("EXTERNALREFID") and mbo.getMboSet("X4APPRLABOR").isEmpty():
        mbo.setValue("X4STORNOSAP","S")
        
mbo.getThisMboSet().save()
mbo.getThisMboSet().reset()
    
'''
mbo.setValue("STATUS", "APERTO",11L)

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
sameWoMbo.setValue("SCHEDSTART", None)
sameWoMbo.setValue("SCHEDFINISH", None)
sameWoMbo.setValue("ACTSTART", None)
sameWoMbo.setValue("ACTFINISH", None)
sameWoMbo.setValue("X4FISICO","0")
sameWoMbo.setValue("X4STORNOSAP", "T")
sameWoMbo.setValue("X4MODULICAR","0")
sameWoMbo.setValue("X4MODULICAR","0")
sameWoMbo.setValue("X4ANAGRAFICAUPDATE","0")

sameWo.save()

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


#sameWo.save()
#sameWo.close()
labtrans_set.close()


labtrans_sap=mbo.getMboSet("$LABTRANSSAP$","LABTRANS","refwo=:WONUM and siteid=:SITEID and X4CHECKSAP='OK'")
if labtrans_sap.isEmpty():
    PublishChannelCache.getInstance().getPublishChannel("X4STORNOGEOCALL").publish(mbo, True);
#wfSrv = MXServer.getMXServer().lookup('WORKFLOW')
#wfSrv.initiateWorkflow(workflownome, mbo)