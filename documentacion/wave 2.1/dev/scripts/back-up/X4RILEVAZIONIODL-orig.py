from psdi.server import MXServer

stato = mbo.getString("STATUS")
workorderid=mbo.getLong("WORKORDERID")
#rilevazioniSet = MXServer.getMXServer().getMboSet("X4RILEVAZIONIODL", mbo.getUserInfo())
#_query = "workorderid="+str(workorderid)
#rilevazioniSet.setWhere(_query)
#rilevazioniSet.reset()
rilevazioniSet = mbo.getMboSet("$RILODL$","X4RILEVAZIONIODL","workorderid=:workorderid")

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
    micro=mbo.getString("X4MICROCONTROLLO")
    classIdAsset = asset_set.getMbo(0).getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
    if classIdAsset == "CAMPATA AEREA" or "SOSTEGNO AEREO":
        asset_ancestorSet=asset_set.getMbo(0).getMboSet("$AANCESTOR$","ASSET","(ancestor=:assetnum or assetnum=:assetnum or assetnum in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
        asset_ancestorSostSet=asset_set.getMbo(0).getMboSet("$ASOSTANCESTOR$","ASSET","(ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
        for i in range(0,asset_ancestorSet.count()):
            assetI=asset_ancestorSet.getMbo(i)
            assetNum=assetI.getString("ASSETNUM")
            assetDesc=assetI.getString("DESCRIPTION")
            classIdFiglio = assetI.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
            recordMisExt=assetI.getMboSet("$X4MIS$","X4CATMISURE","x4catmisureid in (select x4catmisureid from x4catmisureclassifications where classificationid='"+str(classIdFiglio)+"') and tipoparametro='Misura'")
            if not recordMisExt.isEmpty():
                for m in range (0,recordMisExt.count()):
                    rilevazioniNew = rilevazioniSet.add()
                    misuraMbo=recordMisExt.getMbo(m)
                    rilevazioniNew.setValue("ASSETNUM", assetNum)
                    rilevazioniNew.setValue("ASSETDESC", assetDesc)
                    rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                    rilevazioniNew.setValue("ID", misuraMbo.getString("IDMISURA"))
                    rilevazioniNew.setValue("DESCRIPTION", misuraMbo.getString("DESCRIPTION"))
                    rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                    rilevazioniNew.setValue("TIPO", "MIS")
                    rilevazioniNew.setValue("WONUM", misuraMbo.getString("UM"))
                    if m==0:
                        rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                        
            queryI = "x4catdifforid in (select x4catdifforid from X4DIFFORLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and iddifformita in (select iddifformita from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            diffAppl = mbo.getMboSet("$diffI$", "X4CATDIFFOR", queryI)
            if not diffAppl.isEmpty():
                for j in range (0,diffAppl.count()):
                    diffMbo = diffAppl.getMbo(j)
                    idDiff = diffMbo.getString("IDDIFFORMITA")
                    rilevazioniNew = rilevazioniSet.add()
                    rilevazioniNew.setValue("ASSETNUM", assetNum)
                    rilevazioniNew.setValue("ASSETDESC", assetDesc)
                    if j==0:
                        rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                    rilevazioniNew.setValue("ID", idDiff)
                    rilevazioniNew.setValue("DESCRIPTION", diffMbo.getString("DESCRIPTION"))
                    rilevazioniNew.setValue("TIPO", "DIFF")
                    rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                    rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                    
            queryIntI = "x4catinterfid in (select x4catinterfid from X4INTERFCLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            intAppl = mbo.getMboSet("$IntI$", "X4CATINTERF", queryIntI)
            if not intAppl.isEmpty():
                for k in range (0,intAppl.count()):
                    intMbo = intAppl.getMbo(k)
                    riferimento=None
                    classificazioneAsset=assetI.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
                    '''if assetI:
                        if classificazioneAsset=="CONDUTTORE" or classificazioneAsset=="ARMAMENTO CONDUTTORE":
                            specSet=assetI.getMboSet("$SP1$","ASSETSPEC","assetnum=:assetnum and assetattrid='LIN_RIF_FASE'")
                            if not specSet.isEmpty():
                                riferimento=specSet.getMbo(0).getString("ALNVALUE")
                    
                        elif classificazioneAsset=="FUNE DI GUARDIA" or classificazioneAsset=="ARMAMENTO FUNE DI GUARDIA":
                            specSet=assetI.getMboSet("$SP1$","ASSETSPEC","assetnum=:assetnum and assetattrid='LIN_RIF_FUNE'")
                            if not specSet.isEmpty():
                                riferimento=specSet.getMbo(0).getString("ALNVALUE")
                        else:
                            check1Set=assetI.getMboSet("$SP1$","ASSETSPEC","assetnum=:assetnum and assetattrid in ('LIN_RIF_ASSET_ARM_COND','LIN_RIF_ASSET_ARM_FDG','LIN_RIF_ASSET_COND','LIN_RIF_ASSET_FDG')")
                            check2Set=assetI.getMboSet("$SP2$","CLASSANCESTOR","classstructureid=:classstructureid and ancestorclassid='DISPOSITIVI E COMPONENTI'")
                            if not check1Set.isEmpty():
                                specSet=check1Set.getMbo(0).getMboSet("$SP3$","ASSETSPEC","assetnum=:tablevalue and assetattrid in('LIN_RIF_FUNE','LIN_RIF_FASE')")
                                if not specSet.isEmpty():
                                    riferimento=specSet.getMbo(0).getString("ALNVALUE")
                            elif not check2Set.isEmpty():
                                riferimento=assetMbo.getString("X3RIFERIMENTOFASE")'''
                    idInt = intMbo.getString("IDINTERFERENZA")
                    rilevazioniNew = rilevazioniSet.add()
                    rilevazioniNew.setValue("ASSETNUM", assetNum)
                    rilevazioniNew.setValue("ASSETDESC", assetDesc)
                    rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                    if k==0:
                        rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                    rilevazioniNew.setValue("ID", idInt)
                    rilevazioniNew.setValue("DESCRIPTION", intMbo.getString("DESCRIPTION"))
                    rilevazioniNew.setValue("TIPO", "INT")
                    rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
        for iS in range(0,asset_ancestorSostSet.count()):
            assetI=asset_ancestorSostSet.getMbo(iS)
            assetNum=assetI.getString("ASSETNUM")
            assetDesc=assetI.getString("DESCRIPTION")
            classIdFiglio = assetI.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
            recordMisExt=assetI.getMboSet("$X4MIS$","X4CATMISURE","x4catmisureid in (select x4catmisureid from x4catmisureclassifications where classificationid='"+str(classIdFiglio)+"') and tipoparametro='Misura'")
            if not recordMisExt.isEmpty():
                for m2 in range (0,recordMisExt.count()):
                    rilevazioniNew = rilevazioniSet.add()
                    misuraMbo=recordMisExt.getMbo(m2)
                    rilevazioniNew.setValue("ASSETNUM", assetNum)
                    rilevazioniNew.setValue("ASSETDESC", assetDesc)
                    rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                    rilevazioniNew.setValue("ID", misuraMbo.getString("IDMISURA"))
                    rilevazioniNew.setValue("DESCRIPTION", misuraMbo.getString("DESCRIPTION"))
                    rilevazioniNew.setValue("TIPO", "MIS")
                    rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                    rilevazioniNew.setValue("WONUM", misuraMbo.getString("UM"))
                    if m2==0:
                        rilevazioniNew.setValue("CLASSIFICATIONID", "1")
            queryI = "x4catdifforid in (select x4catdifforid from X4DIFFORLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and iddifformita in (select iddifformita from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            diffAppl = mbo.getMboSet("$diffI$", "X4CATDIFFOR", queryI)
            if not diffAppl.isEmpty():
                for jS in range (0,diffAppl.count()):
                    diffMbo = diffAppl.getMbo(jS)
                    idDiff = diffMbo.getString("IDDIFFORMITA")
                    rilevazioniNew = rilevazioniSet.add()
                    rilevazioniNew.setValue("ASSETNUM", assetNum)
                    rilevazioniNew.setValue("ASSETDESC", assetDesc)
                    rilevazioniNew.setValue("ID", idDiff)
                    if jS==0:
                        rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                    rilevazioniNew.setValue("DESCRIPTION", diffMbo.getString("DESCRIPTION"))
                    rilevazioniNew.setValue("TIPO", "DIFF")
                    rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                    rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
            queryIntI = "x4catinterfid in (select x4catinterfid from X4INTERFCLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            intAppl = mbo.getMboSet("$IntI$", "X4CATINTERF", queryIntI)
            if not intAppl.isEmpty():
                for kS in range (0,intAppl.count()):
                    intMbo = intAppl.getMbo(kS)
                    idInt = intMbo.getString("IDINTERFERENZA")
                    rilevazioniNew = rilevazioniSet.add()
                    rilevazioniNew.setValue("ASSETNUM", assetNum)
                    rilevazioniNew.setValue("ASSETDESC", assetDesc)
                    rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                    rilevazioniNew.setValue("ID", idInt)
                    if kS==0:
                        rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                    rilevazioniNew.setValue("DESCRIPTION", intMbo.getString("DESCRIPTION"))
                    rilevazioniNew.setValue("TIPO", "INT")
                    rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
        rilevazioniSet.save()
    else:
        asset_ancestorSet=asset_set.getMbo(0).getMboSet("$AANCESTOR$","ASSET","(asset=:assetnum or parent=:parent and siteid=:siteid")
        for b in range (0,asset_ancestorSet.count()):
            classIdAsset=asset_ancestorSet.getMbo(b).getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
            assetnumRil=asset_ancestorSet.getMbo(b).getString("ASSETNUM")
            assetDesc=asset_ancestorSet.getMbo(b).getString("DESCRIPTION")
            recordMisExt=asset_ancestorSet.getMbo(b).getMboSet("$X4MIS$","X4CATMISURE","x4catmisureid in (select x4catmisureid from x4catmisureclassifications where classificationid='"+str(classIdAsset)+"') and tipoparametro='Misura'")
            if not recordMisExt.isEmpty():
                for m3 in range (0,recordMisExt.count()):
                    rilevazioniNew = rilevazioniSet.add()
                    misuraMbo=recordMisExt.getMbo(m3)
                    rilevazioniNew.setValue("ASSETNUM", assetNum)
                    rilevazioniNew.setValue("ASSETDESC", assetDesc)
                    rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                    rilevazioniNew.setValue("ID", misuraMbo.getString("IDMISURA"))
                    rilevazioniNew.setValue("DESCRIPTION", misuraMbo.getString("DESCRIPTION"))
                    rilevazioniNew.setValue("TIPO", "MIS")
                    rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                    rilevazioniNew.setValue("WONUM", misuraMbo.getString("UM"))
                    if m3==0:
                        rilevazioniNew.setValue("CLASSIFICATIONID", "1")
            query = "x4catdifforid in (select x4catdifforid from X4DIFFORLASSIFICATIONS where classificationid='"+str(classIdAsset)+"') and status='ATTIVO' and iddifformita in (select iddifformita from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            diffAppl = mbo.getMboSet("$diff$", "X4CATDIFFOR", query)
            if not diffAppl.isEmpty():
                for nm in range (0,diffAppl.count()):
                    diffMbo = diffAppl.getMbo(nm)
                    idDiff = diffMbo.getString("IDDIFFORMITA")
                    rilevazioniNew = rilevazioniSet.add()
                    rilevazioniNew.setValue("ASSETNUM", assetnumRil)
                    rilevazioniNew.setValue("ASSETDESC", assetDesc)
                    rilevazioniNew.setValue("ID", idDiff)
                    if nm==0:
                        rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                    rilevazioniNew.setValue("DESCRIPTION", diffMbo.getString("DESCRIPTION"))
                    rilevazioniNew.setValue("TIPO", "DIFF")
                    rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(asset_ancestorSet.getMbo(b))))
                    rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
            queryInt = "x4catinterfid in (select x4catinterfid from X4INTERFCLASSIFICATIONS where classificationid='"+str(classIdAsset)+"') and status='ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            intAppl = mbo.getMboSet("$IntI$", "X4CATINTERF", queryInt)
            if not intAppl.isEmpty():
                for nk in range (0,intAppl.count()):
                    intMbo = intAppl.getMbo(nk)
                    idInt = intMbo.getString("IDINTERFERENZA")
                    rilevazioniNew = rilevazioniSet.add()
                    rilevazioniNew.setValue("ASSETNUM", assetnumRil)
                    rilevazioniNew.setValue("ASSETDESC", assetDesc)
                    rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(asset_ancestorSet.getMbo(b))))
                    rilevazioniNew.setValue("ID", idInt)
                    if nk==0:
                        rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                    rilevazioniNew.setValue("DESCRIPTION", intMbo.getString("DESCRIPTION"))
                    rilevazioniNew.setValue("TIPO", "INT")
                    rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
            #rilevazioniSet.save()