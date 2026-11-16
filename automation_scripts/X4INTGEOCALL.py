from psdi.mbo import MboConstants
from psdi.server import MXServer
from psdi.common.context import UIContext

def yes():
    status = mbo.getString("STATUS")
    wonum=mbo.getString("WONUM")
    parent=mbo.getString("PARENT")
    '''
    if status in ["SCHEDULABILE", "SCHEDULATO"]:
        mbo.changeStatus("APERTO", MXServer.getMXServer().getDate(), "Annullato OdL su Geocall")
        mbo.setValueNull("SCHEDSTART")
        mbo.setValueNull("SCHEDFINISH")
        mbo.setValueNull("PARENT")
        
        # Evoca un canale di pubblicazione
        where_clause= "wonum='%s' and siteid='%s'" % (mbo.getString("WONUM"), mbo.getString("SITEID"))
        MXServer.getMXServer().lookup("MIC").exportData(
            "X4CANCELOPERATION",
            "X4CANCELOPERATION", 
            where_clause,
            mbo.getUserInfo(),
            1
        )
        '''
    if status == "APERTO" or status=="DA VALIDARE":
        efforti=mbo.getDouble("X4EFFORTI")
        effortq=mbo.getDouble("X4EFFORTQ")
        efforto=mbo.getDouble("X4EFFORTO")
        ttrasf=mbo.getDouble("X4TTRASF")
        fisicoplan=mbo.getDouble("X4FISICOPLAN")
        parentSet=mbo.getMboSet("PARENT")
        parentMbo=parentSet.getMbo(0)
        Pefforti=parentMbo.getDouble("X4EFFORTI")
        Peffortq=parentMbo.getDouble("X4EFFORTQ")
        Pefforto=parentMbo.getDouble("X4EFFORTO")
        Pttrasf=parentMbo.getDouble("X4TTRASF")
        Pfisicoplan=parentMbo.getDouble("X4FISICOPLAN")
        parentMbo.setValue("X4EFFORTI",Pefforti-efforti)
        parentMbo.setValue("X4EFFORTQ",Peffortq-effortq)
        parentMbo.setValue("X4EFFORTO",Pefforto-efforto)
        parentMbo.setValue("X4FISICOPLAN",Pfisicoplan-fisicoplan)
        parentMbo.setValue("X4TTRASF",Pttrasf-ttrasf)
        
        figliSet = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
        figliSet.setWhere("parent = '%s'" % parent)
        figliSet.reset()
 
        if figliSet.count() == 1:
            parentMbo.setValue("X4AGGREGATI", False)
         
        parentSet.save()
        figliSet.close()
        
        thisWo = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo()) 
        thisWo.setWhere("wonum = '%s'" % wonum)
        thisWo.reset()
        #service.error("TEST",str(thisWo.getMbo(0).getString("WONUM")))
        thisWo.getMbo(0).setValueNull("PARENT")
        thisWo.getMbo(0).setValueNull("X4ODM",11L)
        thisWo.getMbo(0).setValueNull("X4NTK",11L)
        thisWo.getMbo(0).setValueNull("X4NOPERAZIONE",11L)
        thisWo.save()
        #mbo.getThisMboSet().reset()
        '''
        mbo.getThisMboSet().getMbo(0).setValueNull("PARENT")
        mbo.getThisMboSet().save()
        mbo.getThisMboSet().reset()
        '''
        ui = UIContext.getCurrentContext()

        if ui is not None:
            wcs = ui.getWebClientSession()
        
            tableBean = wcs.getDataBean("plans_children_table")
        
            if tableBean is not None:
                tableBean.reloadTable()
                tableBean.refreshTable()
            if app is not None:
                appBean = app.getAppBean()
                appBean.fireDataChangedEvent()
        #appBean = service.webclientsession().getCurrentApp().getBeanForApp()
        #appBean.fireStructureChangedEvent()
        
    elif status !="APERTO":
        service.error("x3terna","ErrorMsg",["Stato dell'ordine di lavoro padre non ammissibile."])

	
def no():
	pass
	
def dflt():
	service.yncerror("x3","GenericYesNo",["Si vuole procedere con lo sganciamento del figlio?"])
	
cases = {service.YNC_NULL: dflt,service.YNC_YES: yes,service.YNC_NO: no}
if interactive:
	x = service.yncuserinput()
	cases[x]()