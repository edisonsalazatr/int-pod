if mbo.isModified("STATUS") and mbo.getString("STATUS")=="SCHEDULABILE":
    statusValue = mbo.getMboValue("STATUS")
    prevValue = statusValue.getPreviousValue().asString()
    if prevValue=="SCHEDULATO":
        assignments_set = mbo.getMboSet("ASSIGNMENT")
        assignments_set.deleteAll()