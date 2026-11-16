from psdi.iface.mic import PublishChannelCache
#indispwo_set = mbo.getMboSet("X4INDISP")
jpSet=mbo.getMboSet("JOBPLAN")
# if mbo.getString("WONUM") == "1456":
'''
if mbo.isModified("JPNUM") and not jpSet.isEmpty() and not mbo.isNull("JPNUM") and not mbo.isNull("X4STATUSINDISP"):
    
    indispwo_list = [
        (indispwo_set.getMbo(i), indispwo_set.getMbo(i).getInt("X4INDISPWOID")) \
            for i in range(0, indispwo_set.count())]
    # indispwo_set.setWhere("X4INDISPWOID = %s" % max_id)
    # Prendi indispwo con il numero massimo di X4INDISPWOID
    indispwo = sorted(indispwo_list, key=lambda x: x[1], reverse=True)[0][0]
    if indispwo.getString("STATUS") != "ANNULLATO":
        jpnum = mbo.getString("JPNUM")
        jpnum_prev = mbo.getMboValue("JPNUM").getPreviousValue().asString()
        # JPNUM isModified e il valore precedente non era Null 
        # (ma diverso sempre da quello corrente che sto impostando)
        if mbo.isModified("JPNUM") and jpnum != jpnum_prev and not mbo.isNull("JPNUM"):
            indispwo.setValue("MOTIVOANN", "EAM")
            indispwo.setValue("STATUS", "ANNULLATO")
            #mbo.setValue("X4STATUSINDISP","ANNULLATO")
            #mbo.setValue("X4MOTIVOINDISP","EAM")
            
            mbo.setValue("X4STATUSINDISP","ANNULLATO")
            mbo.setValue("X4MOTIVOINDISP","EAM")
            indispwo_set.save()
    
    jpMbo=jpSet.getMbo(0)
    if (jpMbo.getBoolean("X4INDISPONIBILITA")==False or mbo.isModified("X4TIPOSPESA") or mbo.isModified("X4WBS") or mbo.isModified("X4WBE")) and not mbo.isNull("TARGSTARTDATE"):
        mbo.getThisMboSet().save()
        PublishChannelCache.getInstance().getPublishChannel("X4ODLAGILE2").publish(mbo, True);
'''  
if not jpSet.isEmpty() and onupdate:
    jpMbo=jpSet.getMbo(0)
    if jpMbo.getBoolean("X4INDISPONIBILITA")==True:
        if mbo.isModified("X4WBS") or mbo.isModified("X4WBE"):
                PublishChannelCache.getInstance().getPublishChannel("X4ODLAGILE2").publish(mbo, True);
        if mbo.isModified("SCHEDSTART") or mbo.isModified("SCHEDFINISH"):
            PublishChannelCache.getInstance().getPublishChannel("X4ODLAGILE2").publish(mbo, True);
        if mbo.isModified("STATUS") and not mbo.isModified("SCHEDSTART") and mbo.getString("STATUS")=="NON ESEGUITO" and onupdate:
            PublishChannelCache.getInstance().getPublishChannel("X4ODLAGILE2").publish(mbo, True);
    if mbo.isModified("JPNUM") and not mbo.isNull("X4STATUSINDISP"):
        PublishChannelCache.getInstance().getPublishChannel("X4ODLAGILE2").publish(mbo, True);
    if mbo.isModified("REASONFORCHANGE"):
        PublishChannelCache.getInstance().getPublishChannel("X4ODLAGILE2").publish(mbo, True);
#indispwo_set.close()

'''
if mbo.isModified("OWNERGROUP"):
    mboValue = mbo.getMboValue("OWNERGROUP")
    initValue = mboValue.getInitialValue().asString()
    ownergroup=mboValue.getCurrentValue().asString()
    datainizio=mbo.getDate("TARGSTARTDATE")
    datafine=mbo.getDate("TARGCOMPDATE")
    if initValue =="":
        wpLaborSet=mbo.getMboSet("WPLABOR")
        if not wpLaborSet.isEmpty():
            for i in range (0, wpLaborSet.count()):
                wpLabor=wpLaborSet.getMbo(i)
                wpLabor.setValue("X4TITRIS",ownergroup)
                wpLabor.setValue("X4DATAINIZIORISORSA",datainizio)
                wpLabor.setValue("X4DATAFINERISORSA",datafine)
            wpLaborSet.save()
        wpLaborSet.close()