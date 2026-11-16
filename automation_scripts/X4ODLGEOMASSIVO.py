from psdi.mbo import MboConstants
from psdi.server import MXServer
from psdi.iface.mic import PublishChannelCache

session = service.webclientsession()
app = session.getCurrentApp()
if app is None:
    service.error("AppError: ", "L'oggetto 'app' non è inizializzato")
wo_dialog_set = session.getDataBean("17195561296682")
if wo_dialog_set is None:
    service.error("DialogError: ", "Dialog %s non esistente" % "X4ODLGEOMASSIVO")
wonum=[]
selected_workorders = wo_dialog_set.getSelection()
for i in range(0, selected_workorders.size()):
		selected_workorder = selected_workorders.get(i)
		
	     # Salta l'ODL se manca una delle due date
		if selected_workorder.getDate("TARGSTARTDATE") is None or selected_workorder.getDate("TARGCOMPDATE") is None:
		    continue
		    
		wonum.append(selected_workorder.getString("WONUM"))
		#workorder=mbo.getMboSet("$X4WO$","WORKORDER","wonum='"+str(selected_workorder.getString("WONUM"))+"' and siteid='"+str(selected_workorder.getString("SITEID"))+"'")
		#workorder_mbo=workorder.getMbo(0)
		selected_workorder.changeStatus("SCHEDULABILE", MXServer.getMXServer().getDate(), "Invio Massivo vs Geocall")
		figliSet=selected_workorder.getMboSet("CHILDNOTASK")
		if not figliSet.isEmpty():
		    for j in range (0,figliSet.count()):
				figliMbo=figliSet.getMbo(j)
				if figliMbo.getString("STATUS")=="APERTO":
				    figliMbo.changeStatus("SCHEDULABILE", MXServer.getMXServer().getDate(), "Invio Massivo vs Geocall")
		#selected_workorder.setValue("STATUS","SCHEDULABILE",11L)
		#selected_workorder.isModified()
		#workorder.save()
wo_dialog_set.getMboSet().save()
#mbo.getThisMboSet().save()
#selected_workorders.save()
'''for k in wonum:
    woSet=MXServer.getMXServer().getMboSet("WORKORDER", MXServer.getMXServer().getSystemUserInfo())
    woSet.setWhere("wonum='"+k+"' and siteid='SIT01'")
    woMbo=woSet.getMbo(0)
    PublishChannelCache.getInstance().getPublishChannel("X4INTODLGEOCALL").publish(woMbo, True);
    woSet.save()'''
service.closeDialog()