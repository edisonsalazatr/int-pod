if launchPoint=="X4RENAMESAPIDWO":
    newsapid=mbo.getString("SAPID_DEST")
    oldsapid=mbo.getString("SAPID_ORIG")
    assetnum=mbo.getString("ASSETNUM")
    
    conKey = mbo.getThisMboSet().getUserInfo().getConnectionKey();
    con = mbo.getThisMboSet().getMboServer().getDBConnection(conKey)
    s = con.createStatement()
    val = "update WORKORDER set WOEQ9 = '"+str(newsapid)+"' where assetnum ='"+str(assetnum)+"' and istask=0 and historyflag=0"
    rs = s.execute(val)
    con.commit()
    
elif launchPoint=="X4RENAMESAPIDASSET":
    if mbo.isModified("X3SAPID"):
        newsapid=mbo.getString("X3SAPID")
        assetnum=mbo.getString("ASSETNUM")
        
        conKey = mbo.getThisMboSet().getUserInfo().getConnectionKey();
        con = mbo.getThisMboSet().getMboServer().getDBConnection(conKey)
        s = con.createStatement()
        val = "update WORKORDER set WOEQ9 = '"+str(newsapid)+"' where assetnum ='"+str(assetnum)+"' and istask=0 and historyflag=0"
        rs = s.execute(val)
        con.commit()
        
elif launchPoint=="X4RENAMESAPIDASSETLOC":
    if onadd:
        newsapid=mbo.getString("X3SAPID")
        assetnum=mbo.getString("TARGETASSETNUM")
        
        checkExtWo=mbo.getMboSet("$WOANC$","WORKORDER","istask=0 and historyflag=0 and assetnum in (select assetnum from asset where assetnum=:targetassetnum or ancestor=:targetassetnum)")
        
        if not checkExtWo.isEmpty():
            conKey = mbo.getThisMboSet().getUserInfo().getConnectionKey();
            con = mbo.getThisMboSet().getMboServer().getDBConnection(conKey)
            s = con.createStatement()
            val = "update WORKORDER set WOEQ9 = '"+str(newsapid)+"' where istask=0 and historyflag=0 and assetnum in (select assetnum from asset where assetnum='"+str(assetnum)+"' or ancestor='"+str(assetnum)+"')"
            rs = s.execute(val)
            con.commit()