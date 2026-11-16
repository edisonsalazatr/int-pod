from psdi.server import MXServer
if mbo.getString("STATUS")=="COMP" or mbo.getString("STATUS")=="ESEGUITO":
    microSet=mbo.getMboSet("X4MICROCONTROLLO")
    if not microSet.isEmpty():
        microMbo=microSet.getMbo(0)
        if microMbo.getBoolean("MISUREOBB")==True and not microMbo.getMboSet("X4ASSOCMISURA").isEmpty():
            checkMisObb=mbo.getMboSet("$MISMICRO$","X4MICROCONTROLLI",":x4microcontrollo=idmicrocontrollo and misureobb=1 and exists (SELECT 1 FROM x4rilevazioniodl WHERE tipo='MIS' AND workorderid=:workorderid AND (id,assetnum) NOT IN (SELECT idmisura,assetnum FROM x4misurewo WHERE wonuM=:wonum))")
            if not checkMisObb.isEmpty():
                service.error("x3terna", "ErrorMsg", ["Si prega di inserire tutte le misure obbligatorie per il microcontrollo."])
    if mbo.getString("STATUS")=="COMP":
        mbo.setValue("STATUS","ESEGUITO",11L)
        mbo.getThisMboSet().save()
    
if mbo.isNull("X4CLASSASSET") and launchPoint=="X4WOESEGUITO_TASK2":
    assetSet=mbo.getMboSet("ASSET")
    assetMbo=assetSet.getMbo(0)
    classificationID=assetMbo.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
    mbo.setValue("X4CLASSASSET",classificationID,11L)
    if mbo.isNull("X4SAPIDASSET"):
        parenAssetSet=assetMbo.getMboSet("PARENT")
        if not parenAssetSet.isEmpty():
            parenAssetMbo=parenAssetSet.getMbo(0)
            mbo.setValue("X4SAPIDASSET",parenAssetMbo.getString("X3SAPID"))
    assetSet.close()
    
if mbo.isNull("X4NOTE") and mbo.isModified("X4NOTE") and launchPoint=="X4WOESEGUITO_TASK2":
    mbo.setValue("MILESTONE",0,11L)
    
if mbo.isModified("X4COMPMOBILE") and mbo.getBoolean("X4COMPMOBILE")==True:
    if mbo.isNull("X4MATRICOLAESITO"):
        mbo.setValue("X4MATRICOLAESITO",user)
    checkTipoPdL=mbo.getMboSet("$JOBPLANCONTROLLO$","JOBPLAN","JPNUM=:JPNUM and :PLUSCJPREVNUM=PLUSCREVNUM and classstructureid in (select classstructureid from classstructure where classificationid like 'CONTROLLO%')")
    if not checkTipoPdL.isEmpty():
        wonum=mbo.getString("WONUM")
        difforConfermaSet=mbo.getMboSet("$DIFFORCONFERMA$","X4DIFFORSTAT","(IDDIFFORMITA_EXT IN (SELECT difformita FROM X4DIFFATT WHERE jpnum=:jpnum) OR IDDIFFORMITA_EXT IN (SELECT IDDIFFORMITA FROM X3CATASSOCIAZIONE WHERE IDORIGINE=:JPNUM OR IDORIGINE=:X4MICROCONTROLLO)) AND(assetnum in (select assetnum from assetancestor where ancestor=:assetnum) or assetnum in (select assetnum from asset where ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum in ((select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum=:assetnum) and status in('VALIDATA', 'DA VALIDARE') and x4difforstatid not in (select x4difforstatid from x4diffor_wo where wonum=:wonum)")
        if not difforConfermaSet.isEmpty():
            for i in range (0,difforConfermaSet.count()):
                difforConfermaMbo=difforConfermaSet.getMbo(i)
                iddiff=difforConfermaMbo.getString("IDDIFFORMITA_EXT")
                asset=difforConfermaMbo.getString("ASSETNUM")
                gravita=difforConfermaMbo.getString("GRAVITA")
                difforstatid=difforConfermaMbo.getLong("X4DIFFORSTATID")
                fase=difforConfermaMbo.getString("FASE")
                difforConfermaMbo.setValue("MATRICOLARILEVATORE",user,11L)
                difforConfermaMbo.setValue("ULTIMARILEVAZIONE",MXServer.getMXServer().getDate(),11L)
                difforWO=mbo.getMboSet("X4DIFFORMITAWO")
                newDifforWo=difforWO.add()
                newDifforWo.setValue("IDDIFFORMITA",iddiff,11L)
                newDifforWo.setValue("ASSETNUM",asset,11L)
                newDifforWo.setValue("GRAVITA",gravita,11L)
                newDifforWo.setValue("X4DIFFORSTATID",difforstatid)
                newDifforWo.setValue("WONUM",wonum,11L)
                newDifforWo.setValue("SITEID","SIT01",11L)
                newDifforWo.setValue("FASE","fase",11L)
            difforWO.save()
            difforConfermaSet.save()
        interfConfermaSet=mbo.getMboSet("$INTERFCONFERMA$","X4INTERFSTAT","(IDINTERFERENZA_EXT IN (SELECT interferenza FROM x4interfatt WHERE attivita=:jpnum) OR IDINTERFERENZA_EXT IN (SELECT IDINTERFERENZA FROM X3CATASSOCIAZIONE WHERE IDORIGINE=:JPNUM OR IDORIGINE=:X4MICROCONTROLLO)) AND(assetnum in (select assetnum from assetancestor where ancestor=:assetnum) or assetnum in (select assetnum from asset where ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum in ((select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum=:assetnum) and status in('VALIDATA', 'DA VALIDARE') and x4interfstatid not in (select x4interfstatid from x4interf_wo where wonum=:wonum)")
        if not interfConfermaSet.isEmpty():
            for j in range (0,interfConfermaSet.count()):
                interfConfermaMbo=interfConfermaSet.getMbo(j)
                idint=interfConfermaMbo.getString("IDINTERFERENZA_EXT")
                asset=interfConfermaMbo.getString("ASSETNUM")
                gravita=interfConfermaMbo.getString("GRAVITA")
                interfstatid=interfConfermaMbo.getLong("X4INTERFSTATID")
                fase=interfConfermaMbo.getString("FASE")
                interfConfermaMbo.setValue("MATRICOLARILEVATORE",user,11L)
                interfConfermaMbo.setValue("ULTIMARILEVAZIONE",MXServer.getMXServer().getDate(),11L)
                interfWO=mbo.getMboSet("X4INTERFERENZAWO")
                newinterfWO=interfWO.add()
                newinterfWO.setValue("IDINTERFERENZA",idint,11L)
                newinterfWO.setValue("ASSETNUM",asset,11L)
                newinterfWO.setValue("GRAVITA",gravita,11L)
                newinterfWO.setValue("X4INTERFSTATID",interfstatid)
                newinterfWO.setValue("WONUM",wonum,11L)
                newinterfWO.setValue("SITEID","SIT01",11L)
                newinterfWO.setValue("FASE",fase,11L)
            interfWO.save()
            interfConfermaSet.save()
            
if mbo.isModified("STATUS") and mbo.getString("STATUS")=="ESEGUITO" and mbo.getBoolean("X4COMPMOBILE")==False:
    checkTipoPdL=mbo.getMboSet("$JOBPLANCONTROLLO$","JOBPLAN","JPNUM=:JPNUM and :PLUSCJPREVNUM=PLUSCREVNUM and classstructureid in (select classstructureid from classstructure where classificationid like 'CONTROLLO%')")
    if not checkTipoPdL.isEmpty():
        wonum=mbo.getString("WONUM")
        difforConfermaSet=mbo.getMboSet("$DIFFORCONFERMA$","X4DIFFORSTAT","(IDDIFFORMITA_EXT IN (SELECT difformita FROM X4DIFFATT WHERE jpnum=:jpnum) OR IDDIFFORMITA_EXT IN (SELECT IDDIFFORMITA FROM X3CATASSOCIAZIONE WHERE IDORIGINE=:JPNUM OR IDORIGINE=:X4MICROCONTROLLO)) AND(assetnum in (select assetnum from assetancestor where ancestor=:assetnum) or assetnum in (select assetnum from asset where ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum in ((select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum=:assetnum) and status in('VALIDATA', 'DA VALIDARE') and x4difforstatid not in (select x4difforstatid from x4diffor_wo where wonum=:wonum)")
        if not difforConfermaSet.isEmpty():
            for i in range (0,difforConfermaSet.count()):
                difforConfermaMbo=difforConfermaSet.getMbo(i)
                iddiff=difforConfermaMbo.getString("IDDIFFORMITA_EXT")
                asset=difforConfermaMbo.getString("ASSETNUM")
                gravita=difforConfermaMbo.getString("GRAVITA")
                difforstatid=difforConfermaMbo.getLong("X4DIFFORSTATID")
                fase=difforConfermaMbo.getString("FASE")
                difforConfermaMbo.setValue("MATRICOLARILEVATORE",user,11L)
                difforConfermaMbo.setValue("ULTIMARILEVAZIONE",MXServer.getMXServer().getDate(),11L)
                #difforWO=mbo.getMboSet("X4DIFFORMITAWO")
                difforWO=MXServer.getMXServer().getMboSet("X4DIFFOR_WO",MXServer.getMXServer().getSystemUserInfo())
                difforWO.reset()
                newDifforWo=difforWO.add()
                newDifforWo.setValue("IDDIFFORMITA",iddiff,11L)
                newDifforWo.setValue("ASSETNUM",asset,11L)
                newDifforWo.setValue("GRAVITA",gravita,11L)
                newDifforWo.setValue("X4DIFFORSTATID",difforstatid)
                newDifforWo.setValue("WONUM",wonum,11L)
                newDifforWo.setValue("SITEID","SIT01",11L)
                newDifforWo.setValue("FASE","fase",11L)
                difforWO.save()
            #difforConfermaSet.save()
        interfConfermaSet=mbo.getMboSet("$INTERFCONFERMA$","X4INTERFSTAT","(IDINTERFERENZA_EXT IN (SELECT interferenza FROM x4interfatt WHERE attivita=:jpnum) OR IDINTERFERENZA_EXT IN (SELECT IDINTERFERENZA FROM X3CATASSOCIAZIONE WHERE IDORIGINE=:JPNUM OR IDORIGINE=:X4MICROCONTROLLO)) AND(assetnum in (select assetnum from assetancestor where ancestor=:assetnum) or assetnum in (select assetnum from asset where ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum in ((select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum=:assetnum) and status in('VALIDATA', 'DA VALIDARE') and x4interfstatid not in (select x4interfstatid from x4interf_wo where wonum=:wonum)")
        if not interfConfermaSet.isEmpty():
            for j in range (0,interfConfermaSet.count()):
                interfConfermaMbo=interfConfermaSet.getMbo(j)
                idint=interfConfermaMbo.getString("IDINTERFERENZA_EXT")
                asset=interfConfermaMbo.getString("ASSETNUM")
                gravita=interfConfermaMbo.getString("GRAVITA")
                interfstatid=interfConfermaMbo.getLong("X4INTERFSTATID")
                fase=interfConfermaMbo.getString("FASE")
                interfConfermaMbo.setValue("MATRICOLARILEVATORE",user,11L)
                interfConfermaMbo.setValue("ULTIMARILEVAZIONE",MXServer.getMXServer().getDate(),11L)
                #interfWO=mbo.getMboSet("X4INTERFERENZAWO")
                interfWO=MXServer.getMXServer().getMboSet("X4INTERF_WO",MXServer.getMXServer().getSystemUserInfo())
                interfWO.reset()
                newinterfWO=interfWO.add()
                newinterfWO.setValue("IDINTERFERENZA",idint,11L)
                newinterfWO.setValue("ASSETNUM",asset,11L)
                newinterfWO.setValue("GRAVITA",gravita,11L)
                newinterfWO.setValue("X4INTERFSTATID",interfstatid)
                newinterfWO.setValue("WONUM",wonum,11L)
                newinterfWO.setValue("SITEID","SIT01",11L)
                newinterfWO.setValue("FASE",fase,11L)
                interfWO.save()
            #interfConfermaSet.save()