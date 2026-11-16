from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory
logger = MXLoggerFactory.getLogger("maximo.terna")
app = service.webclientsession().getCurrentApp()

if app is None:
    service.error("AppError: ", "L'oggetto 'app' non è inizializzato")

woDialogSet = app.getDataBean()

if woDialogSet is None:
    service.error("DialogError: ", "Dialog non esistente")

mx=MXServer.getMXServer()
ui=MXServer.getMXServer().getSystemUserInfo()
if woDialogSet is None:
    service.error("DialogError: ", "Dialog non esistente")

selectedWOs = woDialogSet.getSelection()
numSelWO  = selectedWOs.size()
for i in range(0,numSelWO):
    try:
        selectedWO = selectedWOs.get(i)
        if  selectedWO.getBoolean("X4GEOCALLCLOSED")==False:
            service.error("x3terna", "ErrorMsg", ["OdL non ancora completato tecnicamente su Geocall. Impossibile procedere."])
        if selectedWO.getBoolean("X4AGGREGATI")==True:
            childSet=selectedWO.getMboSet("CHILDNOTASK")
            if not childSet.isEmpty():
                for j in range(0,childSet.count()):
                    childMbo=childSet.getMbo(j)
                    if childMbo.getString("STATUS")!="NON ESEGUITO" and childMbo.getString("STATUS")!="ESEGUITO":
                    #childMbo.changeStatus("ESEGUITO",MXServer.getMXServer().getDate(), "Eseguito massivo EAM",11L)
                        childMbo.setValue("STATUS","ESEGUITO",11L)
                        childMbo.setValue("ACTFINISH",MXServer.getMXServer().getDate(),11L)
                        chldSetStatus=childMbo.getMboSet("WOSTATUS")
                        newChildStatus=chldSetStatus.add()
                        newChildStatus.setValue("STATUS","ESEGUITO")
                        newChildStatus.setValue("CHANGEDATE",MXServer.getMXServer().getDate(),11L)
                        childSet.save()
            childSet.close()
        taskSet=selectedWO.getMboSet("$TASKMICRO$","WORKORDER","parent=:wonum and siteid=:siteid and istask=1 and status!='ESEGUITO'")
        if not taskSet.isEmpty():
            continue
        '''assignmentSet = mx.getMboSet("WFASSIGNMENT",ui);
        #service.error("TEST","ownerid="+str(mbo.getLong("WORKORDERID"))+" and ownertable='WORKORDER' and ASSIGNSTATUS='ACTIVE' and assigncode='"+str(user)+"'")
        assignmentSet.setWhere("ownerid="+str(selectedWO.getLong("WORKORDERID"))+" and ownertable='WORKORDER' and ASSIGNSTATUS='ACTIVE' and assigncode='"+str(user)+"'");
        if not assignmentSet.isEmpty():
            assignment = assignmentSet.moveFirst();
            MXServer.getMXServer().lookup("WORKFLOW").completeAssignment(assignment, "OdL Eseguito massivamente", 1);
        else:
            wfService = MXServer.getMXServer().lookup("WORKFLOW")
            wfService.initiateWorkflow("X4WOMAIN-H", selectedWO)
            assignmentSet.setWhere("ownerid="+str(selectedWO.getLong("WORKORDERID"))+" and ownertable='WORKORDER' and ASSIGNSTATUS='ACTIVE' and assigncode='"+str(user)+"'");
            assignmentSet.reset()
            if not assignmentSet.isEmpty():
                assignment = assignmentSet.moveFirst();
                MXServer.getMXServer().lookup("WORKFLOW").completeAssignment(assignment, "OdL Eseguito massivamente", 1)'''
        if selectedWO.getBoolean("X4GEOCALLCLOSED")==True and selectedWO.getString("STATUS") in ["AVVIATO","INVIATO","ESEGUITO PARZ."]:
            thisWO=mx.getMboSet("WORKORDER",mbo.getUserInfo())
            thisWO.setWhere("wonum='"+str(selectedWO.getString("WONUM"))+"' and siteid='"+str(selectedWO.getString("SITEID"))+"'")
            thisWO.reset()
            thisWOMbo=thisWO.getMbo(0)
            thisWOMbo.setValue("STATUS","ESEGUITO",11L)
            thisWOMbo.setValue("ACTFINISH",MXServer.getMXServer().getDate(),11L)
            thisWOMbo.setValue("STATUSDATE",MXServer.getMXServer().getDate(),11L)
            SetStatus=thisWOMbo.getMboSet("WOSTATUS")
            newSetStatus=SetStatus.add()
            newSetStatus.setValue("STATUS","ESEGUITO",11L)
            newSetStatus.setValue("CHANGEDATE",MXServer.getMXServer().getDate(),11L)
            thisWO.save()
            thisWO.close()
    except Exception as e:
        logger.info("Errore Eseguito massivo (non bloccante): " + str(e))
            
    finally:
        logger.info("X4ESEGUITOMASS - Next step")
#mbo.getThisMboSet().save()