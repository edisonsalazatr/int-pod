from psdi.server import MXServer

wo_children_set = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
wo_children_set.setWhere("PARENT='%s' AND ISTASK=0" % mbo.getString("WONUM"))
for wo in range(0, wo_children_set.count()):
    wo.setValue("STATUS", "SCHEDULATO")
wo_children_set.save()
wo_children_set.close()