from psdi.server import MXServer

persongroupSet1 = MXServer.getMXServer().getMboSet("PERSONGROUPTEAM", mbo.getUserInfo())
persongroupSet1.setWhere("RESPPARTY='"+user+"' and persongroup in(select persongroup from persongroup where x4tipudb in ('AM','COAM'))")
persongroupSet1.reset()

persongroupSet2 = MXServer.getMXServer().getMboSet("PERSONGROUPTEAM", mbo.getUserInfo())
persongroupSet2.setWhere("RESPPARTY='"+user+"' and persongroup in(select persongroup from persongroup where x4tipudb in ('RL','UI'))")
persongroupSet2.reset()

if not persongroupSet2.isEmpty() and persongroupSet1.isEmpty():
    laborStor=mbo.getString("LABORCODE")
    checkPSet=MXServer.getMXServer().getMboSet("PERSONGROUPTEAM", mbo.getUserInfo())
    checkPSet.setWhere("RESPPARTY='"+laborStor+"' and persongroup in(select persongroup from persongroupteam where respparty='"+user+"')")
    checkPSet.reset()
    if checkPSet.isEmpty():
        service.error("x3terna", "ErrorMsg", ["Risorsa non appartenente alla propria UdB, impossibile stornare."])
        
elif persongroupSet1.isEmpty() and persongroupSet2.isEmpty():
    service.error("x3terna", "ErrorMsg", ["Non si dispongono dei permessi necessari per procedere con lo storno."])
    
persongroupSet1.close()
persongroupSet2.close()

woSet=mbo.getMboSet("WORKORDER")
if not woSet.isEmpty():
    woMbo=woSet.getMbo(0)
refwo = mbo.getString("REFWO")
tipospesa= mbo.getMboSet("WORKORDER").getMbo(0).getString("X4TIPOSPESA")
if tipospesa!="OPEX" and mbo.getString("X4CHECKSAP")=="OK":
    service.error("x3terna", "ErrorMsg", ["Impossibile effettuare storno parziale, verificare che siano presenti consuntivi già validati e che la tipologia spesa sia OPEX."])
if (mbo.isNull("EXTERNALREFID") and mbo.isNull("X4TECMIG"))and mbo.getString("X4CHECKSAP")!="OK":
    service.error("x3terna", "ErrorMsg", ["Impossibile effettuare storno parziale, verificare che siano presenti consuntivi già validati e che la tipologia spesa sia OPEX."])
somma1=0
somma2=0
labCount=mbo.getMboSet("$X4LABTRANSNO$","LABTRANS","refwo=:wonum and x4storno is null")
if not labCount.isEmpty():
    for i in range(0, labCount.count()):
        somma1=somma1+labCount.getMbo(i).getDouble("X4ORETOT")
labCount.close()
labCount2=mbo.getMboSet("$X4LABTRANSNO2$","LABTRANS","refwo='"+str(refwo)+"_S' or (refwo=:wonum and x4storno is not null)")
if not labCount2.isEmpty():
    for j in range(0, labCount2.count()):
        oreattuali=mbo.getDouble("X4ORETOT")
        somma2=somma2+labCount2.getMbo(j).getDouble("X4ORETOT")+oreattuali
labCount2.close()     
somma2=somma2+mbo.getDouble("X4ORETOT")
#service.error("TEST",str(somma1)+" - "+str(somma2))
if float(somma2)>float(somma1*0.7) and not woMbo.isNull("X4FISICO"):
    errorgroup = "X4STORNO"
    errorkey = "X4STORNOPARZ"
    #service.setWarning("X4STORNO", "X4STORNOPARZ", None)

#labtrans_set = MXServer.getMXServer().getMboSet("LABTRANS", mbo.getUserInfo())
#labtrans_set.setWhere("LABTRANSID=%s" % mbo.getInt("LABTRANSID"))
#labtrans_set.reset()

#labtrans = labtrans_set.getMbo(0)
#mbo.setValue("GENAPPRSERVRECEIPT", "0", 11L)
mbo.setValue("X4STORNO", "P", 11L)
if mbo.getString("X4CHECKSAP")!="OK" and not mbo.isNull("EXTERNALREFID"):
    mbo.setValue("GENAPPRSERVRECEIPT", "1", 11L)
#labtrans.setValue("REFWO", refwo+"_S", 11L)
#wo_set = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
#wo_set.setWhere("WONUM='%s'" % (refwo))
#wo_set.reset()

#wo = wo_set.getMbo(0)
#wo.setValue("X4STORNOSAP", "P", 11L)

#wo_set.save()
#wo_set.close()
#labtrans_set.save()
#labtrans_set.close()
#mbo.getThisMboSet().save()