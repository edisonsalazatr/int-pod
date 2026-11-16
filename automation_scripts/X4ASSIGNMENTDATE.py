if mbo.isModified("SCHEDSTART"):
    assSet=mbo.getMboSet("ASSIGNMENT")
    if not assSet.isEmpty():
        for i in range(0,assSet.count()):
            assMbo=assSet.getMbo(i)
            assMbo.setValue("SCHEDULEDATE",mbo.getDate("SCHEDSTART"),11L)
            assMbo.setValue("FINISHDATE",mbo.getDate("SCHEDFINISH"),11L)