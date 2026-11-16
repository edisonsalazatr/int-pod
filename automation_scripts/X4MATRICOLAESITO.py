if mbo.isModified("X4COMPMOBILE") and mbo.getBoolean("X4COMPMOBILE")==True:
    if mbo.isNull("X4MATRICOLAESITO"):
        mbo.setValue("X4MATRICOLAESITO",user)