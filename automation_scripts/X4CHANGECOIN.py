if onadd and not mbo.isNull("X4COIN") and (mbo.isNull("X4TIPOATTIVITA")):
    coinMboSet=mbo.getMboSet("$COIN$","X4COIN",":X4COIN=IDCOIN")
    if not coinMboSet.isEmpty():
        coinMbo=coinMboSet.getMbo(0)
        tipoAttivita=coinMbo.getString("TIPOATTIVITA")
        if mbo.getString("X4TIPOSPESA")!="ANR":
            tipoOrdine=coinMbo.getString("TIPOORDINE")
            mbo.setValue("X4TIPOORDINE",tipoOrdine)
        if mbo.isModified("X4COIN") and mbo.getString("X4TIPOSPESA")=="ANR" and not mbo.isNull("X4TIPOORDINE"):
            mbo.setValueNull("X4TIPOORDINE")
        mbo.setValue("X4TIPOATTIVITA",tipoAttivita)