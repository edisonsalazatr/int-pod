from psdi.server import MXServer
processoUI="X4WO-003-B"
processononUI="X4WO-001-M"
processoMISTO="X4WO-004-A"

workflownome="X4WOMAIN-H"
woSet=mbo.getMboSet("WORKORDER")
if not woSet.isEmpty() and onadd or (onupdate and mbo.isModified("X4TITRIS")):
    workorder=woSet.getMbo(0)
    if not workorder.isNull("PARENT"):
        workorder=workorder.getMboSet("PARENT").getMbo(0)
    status=woSet.getMbo(0).getString("STATUS")
    workorderid=woSet.getMbo(0).getLong("WORKORDERID")
    
    if status=="APERTO" or status=="DA VALIDARE":
        wfInstanceSet=MXServer.getMXServer().getMboSet("WFINSTANCE",MXServer.getMXServer().getSystemUserInfo())
        wfInstanceSet.setWhere("processname='"+str(workflownome)+"' and ownertable='WORKORDER' and ownerid='"+str(workorderid)+"' and active=1")
        wfInstanceSet.reset()
        if not wfInstanceSet.isEmpty():
            wfInstanceMbo=wfInstanceSet.getMbo(0)
            wfInstanceMbo.stopWorkflow("Stopped via script")
            wfInstanceSet.save()

            wfSrv = MXServer.getMXServer().lookup('WORKFLOW')
            wfSrv.initiateWorkflow(workflownome, workorder)
            #mbo.getThisMboSet().save()
        '''
        if onadd:
            workorder=mbo.getMboSet("WORKORDER").getMbo(0)
            wfSet=workorder.getMboSet("WFINSTANCE")
            if not wfSet.isEmpty():
                wfMbo=wfSet.getMbo(0)
                wfUI=wfMbo.getMboSet("$X4ACTIVEASSIGNMENTSUI$","WFASSIGNMENT","wfid = :wfid and assignstatus in (select value from synonymdomain where domainid='WFASGNSTATUS' and maxvalue='ACTIVE') and processname='"+str(processoUI)+"'")
                wfNONUI=wfMbo.getMboSet("$X4ACTIVEASSIGNMENTSNUI$","WFASSIGNMENT","wfid = :wfid and assignstatus in (select value from synonymdomain where domainid='WFASGNSTATUS' and maxvalue='ACTIVE') and processname='"+str(processononUI)+"'")
                wfMISTO=wfMbo.getMboSet("$X4ACTIVEASSIGNMENTSMI$","WFASSIGNMENT","wfid = :wfid and assignstatus in (select value from synonymdomain where domainid='WFASGNSTATUS' and maxvalue='ACTIVE') and processname='"+str(processoMISTO)+"'")