#mbo.setValueNull("FINISHDATE",11L)
newdate=mbo.getDate("STARTDATE")
durata=mbo.getDouble("REGULARHRS")
if mbo.isModified("STARTDATE") and not mbo.isNull("STARTDATE") and mbo.isNull("FINISHDATE"):
    mbo.setValue("FINISHDATE",newdate)
    #mbo.setValue("FINISHDATE",mbo.getDate("STARTDATE"))
    #mbo.setValue("STARTDATE",newdate,11L)
    #mbo.setValue("REGULARHRS",durata,11L)