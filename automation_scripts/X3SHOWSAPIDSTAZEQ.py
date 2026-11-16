from psdi.server import MXServer

userAdmin=['XT4609','SRV_EAM_MIGRAZ_API1','SRV_EAM_MIGRAZ_API2','SRV_EAM_MIGRAZ_API3','SRV_EAM_MIGRAZ_API4','SRV_EAM_MIGRAZ_API5','SRV_EAM_MIGRAZ_API6','SRV_EAM_MIGRAZ_API7','SRV_EAM_MIGRAZ_API8']

userInfo = MXServer.getMXServer().getSystemUserInfo()
user = mbo.getUserInfo().getUserName()
adminSet=MXServer.getMXServer().getMboSet("GROUPUSER",MXServer.getMXServer().getSystemUserInfo())
adminSet.setWhere("(groupname in ('trn-eam-admin-mas-ese','trn-eam-admin-mas-coll') and userid='"+str(user.upper())+"') OR '"+str(user.upper())+"' IN ('MAXADMIN','MXINTADM')")
adminSet.reset()

if adminSet.isEmpty() and interactive:
    classID=mbo.getString("CLASSSTRUCTUREID")
    equipmentMboSet=MXServer.getMXServer().getMboSet("CLASSANCESTOR",MXServer.getMXServer().getSystemUserInfo())
    equipmentMboSet.setWhere("CLASSSTRUCTUREID='"+classID+"' and ancestorclassid='DISPOSITIVI E COMPONENTI'")
    equipmentMboSet.reset()
    
    if not equipmentMboSet.isEmpty():
        x3sapid2=mbo.getString("X3SAPID")
        x3sapid1Set = mbo.getMboSet("PARENT")
        if not x3sapid1Set.isEmpty():
            x3sapid1=x3sapid1Set.getMbo(0).getString("X3SAPID")
            x3sapassetsapid=x3sapid1+"-A-"+x3sapid2
            #sapAssetMboSet=mbo.getMboSet("$SAPASSET$","X3SAPASSET","STRNO='"+str(x3sapassetsapid)+"'")
            #sapAssetMboSet=mbo.getMboSet("$SAPASSET$","X3SAPASSET","X3SAPASSET.X3SAPASSETID IN (SELECT o.SAPASSETID FROM X3SAPTOMAXOBJECT o JOIN ASSET a ON o.OBJECTNUM = a.ASSETNUM AND o.SITEID = a.SITEID JOIN X3MAXIMOSAPMAP m ON m.HIERARCHYPATH = o.HIERARCHYPATH AND m.STEP1 = 1 WHERE o.MAXOBJECT = 'ASSET' AND m.SAPCLASS = X3SAPASSET.SAPCLASS AND o.objectnum=:assetnum and o.siteid=:siteid and o.maxobject='ASSET')")
            sapAssetMboSet=mbo.getMboSet("$SAPASSET$","X3SAPASSET","strno like '%%' || :x3sapid")
            if not sapAssetMboSet.isEmpty():
                sapAssetMbo=sapAssetMboSet.getMbo(0)
                sapid=sapAssetMbo.getString("SAPUID")
                mbo.setValue("X3SAPIDSTAZ",sapid,11L)
                sapAssetMboSet.close()
            else:
                mbo.setValue("X3SAPIDSTAZ","NA",11L)
                sapAssetMboSet.close()
            
    else:
        sapid=mbo.getString("X3SAPID")
        mbo.setValue("X3SAPIDSTAZ",sapid,11L)
    equipmentMboSet.close()
    
adminSet.close()