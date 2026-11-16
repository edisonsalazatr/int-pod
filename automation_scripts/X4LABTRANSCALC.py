if mbo.getString("X4STORNO")=="P" and mbo.getInt("GENAPPRSERVRECEIPT")==1:
    conKey = mbo.getThisMboSet().getUserInfo().getConnectionKey();
    con = mbo.getThisMboSet().getMboServer().getDBConnection(conKey)
    s = con.createStatement()
    val = "update LABTRANS set GENAPPRSERVRECEIPT = 0 where labtransid = '" + str(mbo.getLong("labtransid")) + "' and siteid = '" + mbo.getString("siteid") + "'"
    rs = s.execute(val)
    con.commit()
    
    if s != "":
        s.close()
    if conKey != "" and con != "":
        mbo.getThisMboSet().getMboServer().freeDBConnection(conKey)