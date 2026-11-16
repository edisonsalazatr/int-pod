from psdi.server import MXServer
if (onadd or (onupdate and mbo.isModified("JPNUM"))) and not mbo.getMboSet("WPLABOR").isEmpty() and (mbo.isNull("X4EFFORTO") or mbo.isNull("X4EFFORTQ") or mbo.isNull("X4EFFORTI") or (mbo.getInt("X4EFFORTO")==0 or mbo.getInt("X4EFFORTQ")==0 or mbo.getInt("X4EFFORTI")==0)):# and not mbo.isNull("X4POLICYID"):
    if not mbo.getMboSet("WPLABOR").getMbo(0).toBeDeleted():
        #service.error("test",mbo.getMboSet("WPLABOR").getMbo(0).getString("WONUM"))
        if mbo.getMboSet("WPLABOR").getMbo(0).getBoolean("X4TECMIG")==False:
            childSetNo = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
            childSetNo.setWhere("PARENT='{}'".format(mbo.getString("WONUM")))
            childSetNo.reset()
            effortI=0
            effortO=0
            effortQ=0
            if not childSetNo.isEmpty():
                wpSetParent=mbo.getMboSet("$ALLWOPIAN$","JOBLABOR","jobplanid in (select jobplanid from jobplan where jpnum=:jpnum and status='ACTIVE')")
                conta1=wpSetParent.count()
                for pian in range (0,conta1):
                    pianMbo=wpSetParent.getMbo(pian)
                    if pianMbo.getString("CRAFT")=="IMPIEGAT":
                        effortI=effortI+(pianMbo.getInt("QUANTITY")*pianMbo.getDouble("LABORHRS"))
                    if pianMbo.getString("CRAFT")=="OPERAIO":
                        effortO=effortO+(pianMbo.getInt("QUANTITY")*pianMbo.getDouble("LABORHRS"))
                    if pianMbo.getString("CRAFT")=="QUADRO":
                        effortQ=effortQ+(pianMbo.getInt("QUANTITY")*pianMbo.getDouble("LABORHRS"))
                
                contaFigli=childSetNo.count()
                mbo.setValue("X4EFFORTI",effortI*contaFigli)
                mbo.setValue("X4EFFORTQ",effortQ*contaFigli)
                mbo.setValue("X4EFFORTO",effortO*contaFigli)
                wpSetParent.close()
            else:
                wpSet=mbo.getMboSet("$ALLWOPIAN$","JOBLABOR","jobplanid in (select jobplanid from jobplan where jpnum=:jpnum and status='ACTIVE')")
                conta2=wpSet.count()
                for pian in range (0,conta2):
                    pianMbo=wpSet.getMbo(pian)
                    if pianMbo.getString("CRAFT")=="IMPIEGAT":
                        effortI=effortI+(pianMbo.getInt("QUANTITY")*pianMbo.getDouble("LABORHRS"))
                    if pianMbo.getString("CRAFT")=="OPERAIO":
                        effortO=effortO+(pianMbo.getInt("QUANTITY")*pianMbo.getDouble("LABORHRS"))
                    if pianMbo.getString("CRAFT")=="QUADRO":
                        effortQ=effortQ+(pianMbo.getInt("QUANTITY")*pianMbo.getDouble("LABORHRS"))
                mbo.setValue("X4EFFORTI",effortI)
                mbo.setValue("X4EFFORTQ",effortQ)
                mbo.setValue("X4EFFORTO",effortO)
                wpSet.close()
            #mbo.getThisMboSet().save()
            childSetNo.close()