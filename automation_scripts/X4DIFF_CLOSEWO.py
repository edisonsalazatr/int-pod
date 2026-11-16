"""
05/09/2024
--- Chiusura difformità/interferenze contestualmente a chiusura OdL
Script ACTION -> OBJECT:WORKORDER
Quando viene evocato, si sposta sulla tabella JOBPLAN tramite relazione;
dalla tabella jobplan si sposta alla tabella X4DIFFATT sempre per relazione (getMboSet("X4DIFFATT"))
Se l'mboset della tabella X4DIFFATT NON è vuoto fa il seguente:
ciclo per ogni record della tabella X4DIFFATT, 
si sposta per relazione alla tabella X4DIFFORSTAT dove:
X4DIFFATT.DIFFORMITA=X4DIFFORSTAT.IDDIFFORMITA and X4DIFFATT.ASSETNUM= (assetnum del workorder da cui parte - quindi mbo) and SITEID= (siteid del workorder da cui parte) and X4DIFFORSTAT.STATUS=APERTA or VALIDATA
 
entra in TUTTI gli MBO che si trova e fa 2 cose
cambia STATUS in RISOLTA
e in ODLRISOLUTORE ci mette l'odl da cui è partito lo script
"""
from psdi.server import MXServer

wonum=mbo.getString("WONUM")
stato_test=mbo.getString("STATUS")
if mbo.getBoolean("X4AGGREGATI")==True and mbo.getBoolean("X4PMI")==True:
    mbo.setValue("X4PMI",0,)

def evaluateCalc(jpnum):
    tipoCalcolo=None
    jpMboSet = MXServer.getMXServer().getMboSet("JOBPLAN", mbo.getUserInfo())
    jpMboSet.setWhere("jpnum='"+str(jpnum)+"' and status='ACTIVE'")
    jpMboSet.reset()
    if not jpMboSet.isEmpty():
        jpMbo=jpMboSet.getMbo(0)
        tipoCalcolo=jpMbo.getString("X4UM")
    return tipoCalcolo
    
if mbo.isModified("STATUS") and mbo.getString("STATUS")=="ESEGUITO":
    if not mbo.getMboSet("$MICRO$","WORKORDER","PARENT=:WONUM and istask=1 and status!='ESEGUITO'").isEmpty():
        service.error("x3terna","ErrorMsg", ["Microcontrolli non ancora chiusi."] )

if mbo.isModified("STATUS") and mbo.getString("STATUS")=="ESEGUITO":
    entrato_test="test stato ok"
    
    wo = mbo
    jobplan_set = wo.getMboSet("JOBPLAN")
    if jobplan_set.count():
        jobplan = jobplan_set.getMbo(0)
        
        #Difformita
        x4diffatt_set = jobplan.getMboSet("X4DIFFATT")
        jpnum_test=jobplan.getString("JPNUM")
        revnum_test=jobplan.getString("PLUSCREVNUM")
        diff_test="prima ciclo for diff ris"
        for i in range(0, x4diffatt_set.count()):
            diffris_test="dentro ciclo for diff ris"
            x4diffatt = x4diffatt_set.getMbo(i)
            assetSet=mbo.getMboSet("ASSET")
            assetMbo=assetSet.getMbo(0)
            asset_ancestorSet=assetMbo.getMboSet("$AANCESTOR$","ASSET","assetnum IN ( SELECT q.assetnum FROM asset q WHERE (q.ancestor= :assetnum or (parent=:parent and :classstructureid in (select classstructureid from classancestor where ancestorclassid='LINEA' and classstructureid=:CLASSSTRUCTUREID)) or q.assetnum= :assetnum) and q.siteid= :siteid union select k.ASSETNUM from asset  k where ( k.assetnum in (select targetassetnum from assetlocrelation p where  p.targetassetnum = k.assetnum AND substr(x3sapid,1, length(x3sapid)-2) in (select x3sapid from assetlocrelation where targetassetnum= :assetnum ))) and siteid= :siteid)")
            #asset_ancestorSostSet=mbo.getMboSet("$ASOSTANCESTOR$","ASSET","(ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
            for i2 in range(0, asset_ancestorSet.count()):
                assetChildMbo=asset_ancestorSet.getMbo(i2)
                query_raw_string = "IDDIFFORMITA_EXT='%s' AND ASSETNUM='%s' AND STATUS IN %s"
                x4difforstat_set = mbo.getMboSet("$RISDIFF_%s_%s" % (i, i2), "X4DIFFORSTAT", query_raw_string % (
                    x4diffatt.getString("DIFFORMITA"),
                    assetChildMbo.getString("ASSETNUM"), 
                    "('DA VALIDARE', 'VALIDATA','SOSPESA')",
                ))
                for j in range(0, x4difforstat_set.count()):
                    diffris2_test=x4difforstat_set.count()
                    x4difforstat = x4difforstat_set.getMbo(j)
                    x4difforstat.setValue("STATUS", "RISOLTA",11L)
                    x4difforstat.setValue("ODLRISOLUTORE", mbo.getString("WONUM"),11L)
                #x4difforstat_set.save()
                x4difforstat_set.close()
        #x4diffatt_set.save()
        x4diffatt_set.close()
        var_test="superate difformita risolvibili"
    
        # Interferenze
        x4interfatt_set = jobplan.getMboSet("X4INTERFATT")
        for l in range(0, x4interfatt_set.count()):
            interf_test="dentro ciclo interf"
            x4interf_mbo = x4interfatt_set.getMbo(l)
            assetSet=mbo.getMboSet("ASSET")
            assetMbo=assetSet.getMbo(0)
            asset_ancestorSet=assetMbo.getMboSet("$AANCESTOR$","ASSET","assetnum IN ( SELECT q.assetnum FROM asset q WHERE (q.ancestor= :assetnum or (parent=:parent and :classstructureid in (select classstructureid from classancestor where ancestorclassid='LINEA' and classstructureid=:CLASSSTRUCTUREID)) or q.assetnum= :assetnum) and q.siteid= :siteid union select k.ASSETNUM from asset  k where ( k.assetnum in (select targetassetnum from assetlocrelation p where  p.targetassetnum = k.assetnum AND substr(x3sapid,1, length(x3sapid)-2) in (select x3sapid from assetlocrelation where targetassetnum= :assetnum ))) and siteid= :siteid)")
            #asset_ancestorSostSet=mbo.getMboSet("$ASOSTANCESTOR$","ASSET","(ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
            for l2 in range(0, asset_ancestorSet.count()):
                assetChildMbo=asset_ancestorSet.getMbo(l2)
                query_raw_string = "IDINTERFERENZA_EXT='%s' AND ASSETNUM='%s' AND STATUS IN %s"
                #x4interfstat_set = MXServer.getMXServer().getMboSet("X4INTERFSTAT", mbo.getUserInfo())
                x4interfstat_set = mbo.getMboSet("$RISINT_%s_%s" % (l, l2), "X4INTERFSTAT", query_raw_string % (
                    x4interf_mbo.getString("INTERFERENZA"),
                    assetChildMbo.getString("ASSETNUM"), 
                    "('DA VALIDARE', 'VALIDATA','SOSPESA')",
                ))
                '''x4interfstat_set.setWhere(query_raw_string % (
                    x4interf_mbo.getString("INTERFERENZA"), 
                    assetChildMbo.getString("ASSETNUM"), 
                    "('DA VALIDARE', 'VALIDATA','SOSPESA')",
                ))'''
                for m in range(0, x4interfstat_set.count()):
                    x4interfstat = x4interfstat_set.getMbo(m)
                    x4interfstat.setValue("STATUS", "RISOLTA",11L)
                    x4interfstat.setValue("ODLRISOLUTORE", mbo.getString("WONUM"),11L)
                #x4interfstat_set.save()
                x4interfstat_set.close()
            '''
            for l2S in range(0, asset_ancestorSostSet.count()):
                assetChildMbo=asset_ancestorSostSet.getMbo(l2S)
                query_raw_string = "IDINTERFERENZA_EXT='%s' AND ASSETNUM='%s' AND STATUS IN %s"
                x4interfstat_set = MXServer.getMXServer().getMboSet("X4INTERFSTAT", mbo.getUserInfo())
                x4interfstat_set.setWhere(query_raw_string % (
                    x4interf_mbo.getString("INTERFERENZA"), 
                    assetChildMbo.getString("ASSETNUM"), 
                    "('DA VALIDARE', 'VALIDATA')",
                ))
                for m in range(0, x4interfstat_set.count()):
                    x4interfstat = x4interfstat_set.getMbo(m)
                    x4interfstat.setValue("STATUS", "RISOLTA",11L)
                    x4interfstat.setValue("ODLRISOLUTORE", wo.getString("WONUM"))
                x4interfstat_set.save()
                x4interfstat_set.close()
            '''
        #x4interfatt_set.save()
        x4interfatt_set.close()
    jobplan_set.close()

    diffSetRisolte=mbo.getMboSet("$X4DIFFRIS$","X4DIFFOR_WO","wonum=:wonum and risolta=1")
    interfSetRisolte=mbo.getMboSet("X4INTRIS","X4INTERF_WO","wonum=:wonum and risolta=1")
    
    if not diffSetRisolte.isEmpty():
        for i in range(0,diffSetRisolte.count()):
            asset_num=diffSetRisolte.getMbo(i).getString("ASSETNUM")
            Woset=MXServer.getMXServer().getMboSet("WORKORDER",MXServer.getMXServer().getSystemUserInfo())
            newWo=Woset.add()
            newWo.setValue("ASSETNUM",asset_num,11L)
            newWo.setValue("JPNUM","RIS.CONT.",11L)
            #newWo.setValue("X4FISICOPLAN","1",11L)
            newWo.setValue("X4FISICO","1",11L)
            newWo.setValue("STATUS","CHIUSO",11L)
            newWo.setValue("HISTORYFLAG","1",11L)
            odl=newWo.getString("WONUM")
            difforstatSet=diffSetRisolte.getMbo(i).getMboSet("$DIFFST$","X4DIFFORSTAT","x4difforstatid=:x4difforstatid")
            if not difforstatSet.isEmpty():
                difforstatSet.getMbo(0).setValue("ODLRISOLUTORE", odl,11L)
        #Woset.save()
        Woset.close()
        
    if not interfSetRisolte.isEmpty():
        for i in range(0,interfSetRisolte.count()):
            asset_num=interfSetRisolte.getMbo(i).getString("ASSETNUM")
            Woset=MXServer.getMXServer().getMboSet("WORKORDER",MXServer.getMXServer().getSystemUserInfo())
            newWo=Woset.add()
            newWo.setValue("ASSETNUM",asset_num,11L)
            newWo.setValue("JPNUM","RIS.CONT.",11L)
            #newWo.setValue("X4FISICOPLAN","1",11L)
            newWo.setValue("X4FISICO","1",11L)
            newWo.setValue("STATUS","CHIUSO",11L)
            newWo.setValue("HISTORYFLAG","1",11L)
            odl=newWo.getString("WONUM")
            interfstatSet=interfSetRisolte.getMbo(i).getMboSet("$INTFST$","X4INTERFSTAT","x4interfstatid=:x4interfstatid")
            if not interfstatSet.isEmpty():
                interfstatSet.getMbo(0).setValue("ODLRISOLUTORE", odl,11L)
        #Woset.save()
        Woset.close()
'''
#Calcolo fisico
if not mbo.isNull("PARENT") and mbo.isModified("STATUS") and mbo.getString("STATUS")=="ESEGUITO" and mbo.isNull("X4MICROCONTROLLO"):
    #parentSet=mbo.getMboSet("PARENT")
    parentSet = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
    parentSet.setWhere("wonum='"+str(mbo.getString("PARENT"))+"' and siteid='"+str(mbo.getString("SITEID"))+"'")
    parentWO=parentSet.getMbo(0)
    tipoCalcolo=evaluateCalc(mbo.getString("JPNUM"))
    if tipoCalcolo=="KM":
        #lunghezzaFisico=parentWO.getDouble("X4FISICO")
        mbo.setValue("X4FISICO",mbo.getDouble("X4FISICOPLAN"))
        AssetMboSet=mbo.getMboSet("ASSET")
        if not AssetMboSet.isEmpty():
            AssetMbo=AssetMboSet.getMbo(0)
            AssetCampataSet=AssetMbo.getMboSet("X4ANCESTOR")
            if not AssetCampataSet.isEmpty():
                AssetCampata=AssetCampataSet.getMbo(0)
                LunghezzaMboSet=AssetCampata.getMboSet("$ASSET_LUNG$","ASSETSPEC",":assetnum=assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA'")
                if not LunghezzaMboSet.isEmpty():
                    linLunghezza=LunghezzaMboSet.getMbo(0).getDouble("NUMVALUE")/1000
                    oldfisicoplan=parentWO.getDouble("X4FISICO")
                    somma=oldfisicoplan+linLunghezza
                    parentWO.setValue("X4FISICO",somma)
                    parentSet.save()
    else:
        fisicoPadre=parentWO.getDouble("X4FISICO")
        newFisico=fisicoPadre+mbo.getDouble("X4FISICOPLAN")
        parentWO.setValue("X4FISICO",newFisico)
        mbo.setValue("X4FISICO",mbo.getDouble("X4FISICOPLAN"))
        parentSet.save()

#Calcolo fisico non aggregatori
elif mbo.isModified("STATUS") and mbo.getString("STATUS")=="ESEGUITO" and mbo.getString("X4AGGREGATI")=="0":
    tipoCalcolo=evaluateCalc(mbo.getString("JPNUM"))
    if tipoCalcolo=="KM" and mbo.getString("STATUS")=="ESEGUITO" and mbo.isNull("X4FISICO") and mbo.isNull("PARENT"):
        woSet=MXServer.getMXServer().getMboSet("WORKORDER",MXServer.getMXServer().getSystemUserInfo())
        woSet.setWhere("wonum='"+str(mbo.getString("WONUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"'")
        woSet.reset()
        woSet.getMbo(0).setValue("X4FISICO",mbo.getDouble("X4FISICOPLAN"),11L)
        woSet.save()
        #mbo.getThisMboSet().save()
    elif tipoCalcolo=="QTY" and mbo.getString("STATUS")=="ESEGUITO" and (mbo.isNull("X4FISICO") or mbo.getInt("X4FISICO")==0) and mbo.isNull("PARENT"):
        woSet=MXServer.getMXServer().getMboSet("WORKORDER",MXServer.getMXServer().getSystemUserInfo())
        woSet.setWhere("wonum='"+str(mbo.getString("WONUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"'")
        woSet.reset()
        woSet.getMbo(0).setValue("X4FISICO",woSet.getMbo(0).getDouble("X4FISICOPLAN"),11L)
        woSet.save()
        #mbo.getThisMboSet().save()'''