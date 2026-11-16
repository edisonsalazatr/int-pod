from psdi.server import MXServer

stato=mbo.getString("STATUS")

if (stato=="INOLTRATO" or stato=="SCHEDULATO" or stato=="AVVIATO" or stato=="ESEGUITO PARZ." or stato=="ESEGUIBILE") or (mbo.getBoolean("ISTASK")==True):
    if stato=="INOLTRATO" or stato=="AVVIATO" or (stato=="ESEGUITO PARZ." and not mbo.getMboSet("$WOUI$","WPLABOR","wonum=:wonum and siteid=:siteid and x4titris in (select persongroup from persongroup where x4tipudb='UI')").isEmpty()):
        if mbo.getBoolean("X4GEOCALLCLOSED")==True:
            mbo.changeStatus("ESEGUITO",MXServer.getMXServer().getDate(), "Eseguito EAM")
            mbo.setValue("ACTFINISH",MXServer.getMXServer().getDate())
        else:
            mbo.changeStatus("ESEGUITO",MXServer.getMXServer().getDate(), "Eseguito EAM")
            mbo.setValue("ACTFINISH",MXServer.getMXServer().getDate())
            #service.error("x3terna","ErrorMsg",["Chiusura tecnica non ancora ricevuta da Geocall."])
    else:
        microSet=mbo.getMboSet("X4MICROCONTROLLO")
        if not microSet.isEmpty():
            microMbo=microSet.getMbo(0)
            if microMbo.getBoolean("MISUREOBB")==True and not microMbo.getMboSet("X4ASSOCMISURA").isEmpty():
                checkMisObb=mbo.getMboSet("$MISMICRO$","X4MICROCONTROLLI",":x4microcontrollo=idmicrocontrollo and misureobb=1 and exists (SELECT 1 FROM x4rilevazioniodl WHERE tipo='MIS' AND workorderid=:workorderid AND (id,assetnum) NOT IN (SELECT idmisura,assetnum FROM x4misurewo WHERE wonuM=:wonum))")
                if not checkMisObb.isEmpty():
                    service.error("x3terna", "ErrorMsg", ["Si prega di inserire tutte le misure obbligatorie per il microcontrollo."])
        mbo.changeStatus("ESEGUITO",MXServer.getMXServer().getDate(), "Eseguito EAM")
        mbo.setValue("ACTFINISH",MXServer.getMXServer().getDate())
    appBean = service.webclientsession().getDataBean("1721039515656")
    appBean.fireStructureChangedEvent()
    appBean.fetchTableData() 
else:
    service.error("x3terna","ErrorMsg",["Impossibile procedere con il cambio stato."])