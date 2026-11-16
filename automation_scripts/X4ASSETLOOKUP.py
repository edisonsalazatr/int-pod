if interactive:
    attivita = mbo.getString("JPNUM")
    jobplanSet=mbo.getMboSet("$NEWJP$","JOBPLAN","status='ACTIVE' and jpnum='"+str(attivita)+"'")
    if not jobplanSet.isEmpty():
        jobplanMbo=jobplanSet.getMbo(0)
        livelloAtt=jobplanMbo.getString("X4LIVELLOATTIVITA")
        if livelloAtt=="4" and attivita[3]=="S" and mbo.getBoolean("X4AGGREGATI")==False:
            lookup = "X4ASSETEQ"
        else:
            lookup="X4ASSET"
        mbo.getMboValue("ASSETNUM").setCustomLookupName(lookup)