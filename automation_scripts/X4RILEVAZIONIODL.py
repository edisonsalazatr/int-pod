from psdi.server import MXServer


lib = service.invokeScript("X4EVALCOND").get("X4EvalCondLib")(service)

stato = mbo.getString("STATUS")
workorderid=mbo.getLong("WORKORDERID")
#rilevazioniSet = MXServer.getMXServer().getMboSet("X4RILEVAZIONIODL", mbo.getUserInfo())
#_query = "workorderid="+str(workorderid)
#rilevazioniSet.setWhere(_query)
#rilevazioniSet.reset()
rilevazioniSet = mbo.getMboSet("$RILODL$","X4RILEVAZIONIODL","workorderid=:workorderid")

def sqlValue(value):
    if value is None:
        return ""
    return str(value).replace("'", "''")


def getMisureWhere(classificationid, jpnum, revnum,micro):
    classid = sqlValue(classificationid)
    jp = sqlValue(jpnum)
    mc = sqlValue(micro)
    if not revnum:
        revnum=''
    origineWhere = "((idorigine='" + jp + "' and PLUSCREVNUM='"+ revnum +"') or idorigine='" + mc + "')"
    return "x4catmisureid in (select x4catmisureid from x4catmisureclassifications where classificationid='" + classid + "') and tipoparametro='Misura' and (not exists (select 1 from x4catassociazionemis where " + origineWhere + ") or idmisura in (select idmisura from x4catassociazionemis where " + origineWhere + "))"


def evalRiferimento(assetMbo):
    classificazioneAsset=assetMbo.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
    #riferimento=classificazioneAsset
    riferimento=""
    if classificazioneAsset:
        if classificazioneAsset=="CONDUTTORE" or classificazioneAsset=="ARMAMENTO CONDUTTORE":
            #service.error("TEST",str(classificazioneAsset))
            specSet=assetMbo.getMboSet("$SP1$","ASSETSPEC","assetnum=:assetnum and assetattrid='LIN_RIF_POSIZIONE'")
            if not specSet.isEmpty():
                riferimento=specSet.getMbo(0).getString("NUMVALUE")
                
    
        elif classificazioneAsset=="FUNE DI GUARDIA" or classificazioneAsset=="ARMAMENTO FUNE DI GUARDIA":
            specSet=assetMbo.getMboSet("$SP1$","ASSETSPEC","assetnum=:assetnum and assetattrid='LIN_RIF_FUNE'")
            if not specSet.isEmpty():
                riferimento=specSet.getMbo(0).getString("ALNVALUE")
        else:
            check1Set=assetMbo.getMboSet("$SP1$","ASSETSPEC","assetnum=:assetnum and assetattrid in ('LIN_RIF_ASSET_ARM_COND','LIN_RIF_ASSET_ARM_FDG','LIN_RIF_ASSET_COND','LIN_RIF_ASSET_FDG')")
            check2Set=assetMbo.getMboSet("$SP2$","CLASSANCESTOR","classstructureid=:classstructureid and ancestorclassid='DISPOSITIVI E COMPONENTI'")
            if not check1Set.isEmpty():
                specSet=check1Set.getMbo(0).getMboSet("$SP3$","ASSETSPEC","assetnum=:tablevalue and assetattrid in('LIN_RIF_FUNE','LIN_RIF_POSIZIONE')")
                if not specSet.isEmpty():
                    if specSet.getMbo(0).getString("ASSETATTRID")=="LIN_RIF_FUNE":
                        riferimento=specSet.getMbo(0).getString("ALNVALUE")
                    else:
                        riferimento=specSet.getMbo(0).getString("NUMVALUE")
            elif not check2Set.isEmpty():
                riferimento=assetMbo.getString("X3RIFERIMENTOFASE")
    return riferimento


if rilevazioniSet.isEmpty():
    assetNum = mbo.getString("ASSETNUM")
    asset_set = mbo.getMboSet("ASSET")
    jpnum=mbo.getString("JPNUM")
    revSet=mbo.getMboSet("$JPNUMREV$","JOBPLAN",":jpnum=jpnum and status='ACTIVE'")
    if not revSet.isEmpty():
        revnum=revSet.getMbo(0).getString("PLUSCREVNUM")
    else:
        revnum=''
    micro=mbo.getString("X4MICROCONTROLLO")
    classIdAsset = asset_set.getMbo(0).getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
    if classIdAsset=="CAMPATA AEREA" or classIdAsset=="SOSTEGNO AEREO" or classIdAsset=="ESTREMO CAVO" or classIdAsset=="TRATTA CAVO":
        asset_ancestorSet=asset_set.getMbo(0).getMboSet("$AANCESTOR$","ASSET","assetnum IN ( SELECT q.assetnum FROM asset q WHERE (q.ancestor= :assetnum or q.assetnum= :assetnum) and q.siteid= :siteid union select k.ASSETNUM from asset  k where ( k.assetnum in (select targetassetnum from assetlocrelation p where  p.targetassetnum = k.assetnum AND substr(x3sapid,1, length(x3sapid)-2) in (select x3sapid from assetlocrelation where targetassetnum= :assetnum ))) and siteid= :siteid)")
        asset_ancestorSostSet=asset_set.getMbo(0).getMboSet("$ASOSTANCESTOR$","ASSET","(ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
        for i in range(0,asset_ancestorSet.count()):
            classNew=True
            assetI=asset_ancestorSet.getMbo(i)
            assetNum=assetI.getString("ASSETNUM")
            assetDesc=assetI.getString("DESCRIPTION")
            classIdFiglio = assetI.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
            recordMisExt=assetI.getMboSet("$X4MIS$","X4CATMISURE",getMisureWhere(classIdFiglio,jpnum,revnum,micro))
            if not recordMisExt.isEmpty():
                for m in range (0,recordMisExt.count()):
                    misuraMbo=recordMisExt.getMbo(m)
                    idMis=misuraMbo.getString("IDMISURA")
                    if lib.check(mbo.getUserInfo(),assetNum, "MISURA", idMis):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        rilevazioniNew.setValue("ID", idMis)
                        rilevazioniNew.setValue("DESCRIPTION", misuraMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                        rilevazioniNew.setValue("TIPO", "MIS")
                        rilevazioniNew.setValue("WONUM", misuraMbo.getString("UM")[:25])
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        
            queryI = "x4catdifforid in (select x4catdifforid from X4DIFFORLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and GENMISURA=0 and status='ATTIVO' and iddifformita in (select iddifformita from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            diffAppl = mbo.getMboSet("$diffI$", "X4CATDIFFOR", queryI)
            classNew=True
            if not diffAppl.isEmpty():
                for j in range (0,diffAppl.count()):
                    diffMbo = diffAppl.getMbo(j)
                    idDiff = diffMbo.getString("IDDIFFORMITA")
                    if lib.check(mbo.getUserInfo(),assetNum, "DIFFOR", idDiff):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("ID", idDiff)
                        rilevazioniNew.setValue("DESCRIPTION", diffMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "DIFF")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                    
            queryIntI = "x4catinterfid in (select x4catinterfid from X4INTERFCLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            intAppl = mbo.getMboSet("$IntI$", "X4CATINTERF", queryIntI)
            classNew=True
            if not intAppl.isEmpty():
                for k in range (0,intAppl.count()):
                    intMbo = intAppl.getMbo(k)
                    riferimento=None
                    classificazioneAsset=assetI.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
                    idInt = intMbo.getString("IDINTERFERENZA")
                    if lib.check(mbo.getUserInfo(),assetNum, "INTERF", idInt):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("ID", idInt)
                        rilevazioniNew.setValue("DESCRIPTION", intMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "INT")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
        for iS in range(0,asset_ancestorSostSet.count()):
            assetI=asset_ancestorSostSet.getMbo(iS)
            assetNum=assetI.getString("ASSETNUM")
            assetDesc=assetI.getString("DESCRIPTION")
            classIdFiglio = assetI.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
            classNew=True
            recordMisExt=assetI.getMboSet("$X4MIS$","X4CATMISURE",getMisureWhere(classIdFiglio,jpnum,revnum,micro))
            if not recordMisExt.isEmpty():
                for m2 in range (0,recordMisExt.count()):
                    misuraMbo=recordMisExt.getMbo(m2)
                    idMis=misuraMbo.getString("IDMISURA")
                    if lib.check(mbo.getUserInfo(),assetNum, "MISURA", idMis):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        rilevazioniNew.setValue("ID", misuraMbo.getString("IDMISURA"))
                        rilevazioniNew.setValue("DESCRIPTION", misuraMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "MIS")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                        rilevazioniNew.setValue("WONUM", misuraMbo.getString("UM")[:25])
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
            queryI = "x4catdifforid in (select x4catdifforid from X4DIFFORLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and GENMISURA=0 and  iddifformita in (select iddifformita from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            diffAppl = mbo.getMboSet("$diffI$", "X4CATDIFFOR", queryI)
            classNew=True
            if not diffAppl.isEmpty():
                for jS in range (0,diffAppl.count()):
                    diffMbo = diffAppl.getMbo(jS)
                    idDiff = diffMbo.getString("IDDIFFORMITA")
                    if lib.check(mbo.getUserInfo(),assetNum, "DIFFOR", idDiff):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("ID", idDiff)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("DESCRIPTION", diffMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "DIFF")
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
            queryIntI = "x4catinterfid in (select x4catinterfid from X4INTERFCLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            intAppl = mbo.getMboSet("$IntI$", "X4CATINTERF", queryIntI)
            classNew=True
            if not intAppl.isEmpty():
                for kS in range (0,intAppl.count()):
                    intMbo = intAppl.getMbo(kS)
                    idInt = intMbo.getString("IDINTERFERENZA")
                    if lib.check(mbo.getUserInfo(),assetNum, "INTERF", idInt):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        rilevazioniNew.setValue("ID", idInt)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("DESCRIPTION", intMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "INT")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
        rilevazioniSet.save()
    else:
        if mbo.isNull("X4MICROCONTROLLO"):
            asset_ancestorSet=asset_set.getMbo(0).getMboSet("$AANCESTOR$","ASSET","(assetnum=:assetnum or ((parent=:parent or parent=:assetnum) and :classstructureid in (select classstructureid from classancestor where ancestorclassid='LINEA' or ancestorclassid='DISPOSITIVI E COMPONENTI'))) and siteid=:siteid")
        else:
            asset_ancestorSet=asset_set.getMbo(0).getMboSet("$AANCESTORTOT$","ASSET","assetnum=:assetnum and siteid=:siteid")
        for b in range (0,asset_ancestorSet.count()):
            classIdAsset=asset_ancestorSet.getMbo(b).getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
            assetnumRil=asset_ancestorSet.getMbo(b).getString("ASSETNUM")
            assetDesc=asset_ancestorSet.getMbo(b).getString("DESCRIPTION")
            recordMisExt=asset_ancestorSet.getMbo(b).getMboSet("$X4MIS$","X4CATMISURE",getMisureWhere(classIdAsset,jpnum,revnum,micro))
            classNew=True
            if not recordMisExt.isEmpty():
                for m3 in range (0,recordMisExt.count()):
                    misuraMbo=recordMisExt.getMbo(m3)
                    idMis=misuraMbo.getString("IDMISURA")
                    if lib.check(mbo.getUserInfo(),assetnumRil, "MISURA", idMis):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetnumRil)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(asset_ancestorSet.getMbo(b))))
                        rilevazioniNew.setValue("ID", misuraMbo.getString("IDMISURA"))
                        rilevazioniNew.setValue("DESCRIPTION", misuraMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "MIS")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                        rilevazioniNew.setValue("WONUM", misuraMbo.getString("UM")[:25])
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
            query = "x4catdifforid in (select x4catdifforid from X4DIFFORLASSIFICATIONS where classificationid='"+str(classIdAsset)+"') and status='ATTIVO' and GENMISURA=0 and iddifformita in (select iddifformita from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            diffAppl = mbo.getMboSet("$diff$", "X4CATDIFFOR", query)
            classNew=True
            if not diffAppl.isEmpty():
                for nm in range (0,diffAppl.count()):
                    diffMbo = diffAppl.getMbo(nm)
                    idDiff = diffMbo.getString("IDDIFFORMITA")
                    if lib.check(mbo.getUserInfo(),assetnumRil, "DIFFOR", idDiff):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetnumRil)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("ID", idDiff)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("DESCRIPTION", diffMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "DIFF")
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(asset_ancestorSet.getMbo(b))))
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
            queryInt = "x4catinterfid in (select x4catinterfid from X4INTERFCLASSIFICATIONS where classificationid='"+str(classIdAsset)+"') and status='ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            intAppl = mbo.getMboSet("$IntI$", "X4CATINTERF", queryInt)
            classNew=True
            if not intAppl.isEmpty():
                for nk in range (0,intAppl.count()):
                    intMbo = intAppl.getMbo(nk)
                    idInt = intMbo.getString("IDINTERFERENZA")
                    if lib.check(mbo.getUserInfo(),assetnumRil, "INTERF", idInt):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetnumRil)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(asset_ancestorSet.getMbo(b))))
                        rilevazioniNew.setValue("ID", idInt)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("DESCRIPTION", intMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "INT")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
            #rilevazioniSet.save()