if onupdate and mbo.getBoolean("X4FLAGFISICO")==True and not mbo.isNull("PARENT"):
    woSet=mbo.getMboSet("$WOSET$","WORKORDER","wonum=:parent and x4flagfisico=0 and not exists (select 1 from workorder where parent=:parent and x4flagfisico=0 and workorderid!=:workorderid)")
    if not woSet.isEmpty():
        woMbo=woSet.getMbo(0)
        woMbo.setValue("X4FLAGFISICO","1",11L)
        woSet.save()
    #woSet.close()