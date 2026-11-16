from psdi.server import MXServer
if mbo.isModified("STATUS") and mbo.getString("STATUS")=="NON VALIDATO":
    jobplan_set = mbo.getMboSet("JOBPLAN")
    if jobplan_set.count():
        jobplan = jobplan_set.getMbo(0)
        
        #Difformita
        x4diffatt_set = jobplan.getMboSet("X4DIFFATT")
        for i in range(0, x4diffatt_set.count()):
            x4diffatt = x4diffatt_set.getMbo(i)
            assetSet=mbo.getMboSet("ASSET")
            assetMbo=assetSet.getMbo(0)
            asset_ancestorSet=assetMbo.getMboSet("$AANCESTOR$","ASSET","assetnum IN ( SELECT q.assetnum FROM asset q WHERE (q.ancestor= :assetnum or parent=:parent or q.assetnum= :assetnum) and q.siteid= :siteid union select k.ASSETNUM from asset  k where ( k.assetnum in (select targetassetnum from assetlocrelation p where  p.targetassetnum = k.assetnum AND substr(x3sapid,1, length(x3sapid)-2) in (select x3sapid from assetlocrelation where targetassetnum= :assetnum ))) and siteid= :siteid)")
            #asset_ancestorSostSet=mbo.getMboSet("$ASOSTANCESTOR$","ASSET","(ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
            for i2 in range(0, asset_ancestorSet.count()):
                assetChildMbo=asset_ancestorSet.getMbo(i2)
                query_raw_string = "IDDIFFORMITA_EXT='%s' AND ASSETNUM='%s' AND STATUS IN %s"
                x4difforstat_set = x4diffatt.getMboSet("$X4DIFFATT2X4DIFFORSTAT", "X4DIFFORSTAT", query_raw_string % (
                    x4diffatt.getString("DIFFORMITA"),
                    assetChildMbo.getString("ASSETNUM"), 
                    "('DA VALIDARE', 'VALIDATA')",
                ))
                for j in range(0, x4difforstat_set.count()):
                    x4difforstat = x4difforstat_set.getMbo(j)
                    x4difforstat.setValue("STATUS", "SOSPESA",11L)
                    #x4difforstat.setValue("ODLRISOLUTORE", mbo.getString("WONUM"),11L)
                x4difforstat_set.save()
                x4difforstat_set.close()
        x4diffatt_set.save()
        x4diffatt_set.close()
    
        # Interferenze
        x4interfatt_set = jobplan.getMboSet("X4INTERFATT")
        for l in range(0, x4interfatt_set.count()):
            x4interf_mbo = x4interfatt_set.getMbo(l)
            assetSet=mbo.getMboSet("ASSET")
            assetMbo=assetSet.getMbo(0)
            asset_ancestorSet=assetMbo.getMboSet("$AANCESTOR$","ASSET","assetnum IN ( SELECT q.assetnum FROM asset q WHERE (q.ancestor= :assetnum or parent=:parent or q.assetnum= :assetnum) and q.siteid= :siteid union select k.ASSETNUM from asset  k where ( k.assetnum in (select targetassetnum from assetlocrelation p where  p.targetassetnum = k.assetnum AND substr(x3sapid,1, length(x3sapid)-2) in (select x3sapid from assetlocrelation where targetassetnum= :assetnum ))) and siteid= :siteid)")
            #asset_ancestorSostSet=mbo.getMboSet("$ASOSTANCESTOR$","ASSET","(ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
            for l2 in range(0, asset_ancestorSet.count()):
                assetChildMbo=asset_ancestorSet.getMbo(l2)
                query_raw_string = "IDINTERFERENZA_EXT='%s' AND ASSETNUM='%s' AND STATUS IN %s"
                x4interfstat_set = MXServer.getMXServer().getMboSet("X4INTERFSTAT", mbo.getUserInfo())
                x4interfstat_set.setWhere(query_raw_string % (
                    x4interf_mbo.getString("INTERFERENZA"), 
                    assetChildMbo.getString("ASSETNUM"), 
                    "('DA VALIDARE', 'VALIDATA')",
                ))
                for m in range(0, x4interfstat_set.count()):
                    x4interfstat = x4interfstat_set.getMbo(m)
                    x4interfstat.setValue("STATUS", "SOSPESA",11L)
                    #x4interfstat.setValue("ODLRISOLUTORE", mbo.getString("WONUM"),11L)
                x4interfstat_set.save()
                x4interfstat_set.close()
        x4interfatt_set.save()
        x4interfatt_set.close()
    jobplan_set.close()