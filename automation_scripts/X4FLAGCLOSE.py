moduli=mbo.getBoolean("X4MODULICAR")
anag=mbo.getBoolean("X4ANAGRAFICAUPDATE")
sap=mbo.getBoolean("X4SAPCONSOK")
from psdi.server import MXServer
date=MXServer.getMXServer().getDate()
if mbo.isNull("WOEQ10") and ((moduli==True and anag==False) or (moduli==False and anag==True)) and interactive:
    mbo.setValue("WOEQ10",user,11L)
if moduli==True and anag==True and sap==True and interactive and ((mbo.getInt("WOEQ3")=="1" and not mbo.isNull("EXTERNALREFID")) or mbo.isNull("EXTERNALREFID")):
    def yes():
        mbo.setValue("WOEQ10",user)
        mbo.changeStatus("CHIUSO",date,"Cambio via AS.",11L)
        wfSrv = MXServer.getMXServer().lookup('WORKFLOW')
        wfSet = wfSrv.getActiveInstances(mbo)
        wfSet.getMbo(0).stopWorkflow("Stopped")
        
    def no():
        if launchPoint=="X4FLAGCLOSEANAG":
            mbo.setValue("X4ANAGRAFICAUPDATE",0)
        #elif launchPoint=="X4FLAGCLOSESAP":
            #mbo.setValue("X4ANAGRAFICAUPDATE",0)
        elif launchPoint=="X4FLAGCLOSEMOD":
            mbo.setValue("X4MODULICAR",0)
            
            
    def dflt():
        service.yncerror("x3","GenericYesNo",["L'apposizione di questo flag comporta la chiusura dell'ordine."])
    cases = {service.YNC_NULL:dflt, service.YNC_YES:yes, service.YNC_NO:no}
    if interactive:
        x = service.yncuserinput()
        cases[x]()