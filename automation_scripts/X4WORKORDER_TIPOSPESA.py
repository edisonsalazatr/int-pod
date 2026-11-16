if mbo.isModified("X4TIPOSPESA") and mbo.getBoolean("X4SERBATOIO")==False:
    mbo.setValueNull("X4COIN")
    mbo.setValueNull("X4TIPOORDINE")
    mbo.setValueNull("X4TIPOATTIVITA")
    mbo.setValueNull("X4WBE")
    mbo.setValueNull("X4WBS")