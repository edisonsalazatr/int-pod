if onupdate and (mbo.isModified("TARGSTARTDATE") or mbo.isModified("TARGCOMPDATE")) and not mbo.isNull("X4DINIZIOINDISP") and not mbo.isNull("X4DFINEINDISP") and (mbo.getString("X4STATUSINDISP")=="DELIBERATO" or mbo.getString("X4STATUSINDISP")=="IN ESECUZIONE"):
    X4DINIZIOINDISP = mbo.getDate("X4DINIZIOINDISP")
    X4DFINEINDISP = mbo.getDate("X4DFINEINDISP")
    if (X4DINIZIOINDISP and X4DFINEINDISP) and X4DINIZIOINDISP >= mbo.getDate("TARGSTARTDATE") and X4DFINEINDISP <= mbo.getDate("TARGCOMPDATE"):
        mbo.setValue("X4ESITOINDISP","OK")
        mbo.setValueNull("X4IDERRORINDISP")
        mbo.setValueNull("X4DESCERRORINDISP")
    else:
        mbo.setValue("X4ESITOINDISP","KO")