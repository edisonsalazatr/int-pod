from psdi.server import MXServer

workorderSet=mbo.getMboSet("WORKORDER")

titris=mbo.getString("X4TITRIS")
craft=mbo.getString("CRAFT")
if not workorderSet.isEmpty() and interactive:
    wo = workorderSet.getMbo(0)
    X4SERBATOIO = bool(wo.getInt("X4SERBATOIO"))
    titrisWO = wo.getString("OWNERGROUP")
    if not X4SERBATOIO:
        '''
        if mbo.isNull("X4DATAINIZIORISORSA") or mbo.isNull("X4DATAFINERISORSA") or \
            mbo.isNull("X4TITRIS") or mbo.isNull("CRAFT") and onadd:
            if (mbo.isNull("X4DATAINIZIORISORSA") and mbo.isNull("X4DATAFINERISORSA") and mbo.isNull("X4TITRIS") and onadd):
                mbo.setValue("X4DATAINIZIORISORSA",wo.getDate("TARGSTARTDATE"))
                mbo.setValue("X4DATAFINERISORSA",wo.getDate("TARGCOMPDATE"))
                mbo.setValue("X4TITRIS",wo.getString("OWNERGROUP"))
            else:
                err = "Si prega di compilare i campi: data inizio e data fine risorsa, qualifica e titris."
                service.error("x3terna", "ErrorMsg", [err])
        '''
        if titrisWO != titris and not mbo.isNull("X4TITRIS") and not wo.isNull("OWNERGROUP") and (onadd or (onupdate and (mbo.isModified("X4TITRIS") or mbo.isModified("X4DATAFINERISORSA") or mbo.isModified("CRAFT") or mbo.isModified("LABORHRS")))):
            dataFine=mbo.getDate("X4DATAFINERISORSA")
            mesi = ["GENNAIO", "FEBBRAIO", "MARZO", "APRILE", "MAGGIO", "GIUGNO",
                        "LUGLIO", "AGOSTO", "SETTEMBRE", "OTTOBRE", "NOVEMBRE", "DICEMBRE"]
            mese = mesi[dataFine.getMonth()]
            anno = dataFine.getYear() + 1900
            #service.error("TEST","X4CAPACITYID in (select x4capacityid from x4capacity where anno='"+str(anno)+"') and destinatario='"+str(titrisWO)+"' and mese='"+str(mese)+"' and status='ACCETTATA'")
            capacitySet=mbo.getMboSet("$X4CAPRECORD$","X4CAPACITYEXC","X4CAPACITYID in (select x4capacityid from x4capacity where udb='"+str(titris)+"' and anno='"+str(anno)+"') and destinatario='"+str(titrisWO)+"' and mese='"+str(mese)+"' and craft='"+str(craft)+"' and status='ACCETTATA'")
            if capacitySet.isEmpty():
                service.error("x3terna","ErrorMsg",["Non sono presenti ore cedute dal titris selezionato."])
            else:
                totOre=0
                pianificatoSet=mbo.getMboSet("X4SAMEPRESTATI")
                totPrestati=0
                if not pianificatoSet.isEmpty():
                    for p in range(0,pianificatoSet.count()):
                        pian=pianificatoSet.getMbo(p)
                        totPrestati=totPrestati+(pian.getDouble("X4ORETRASF")+pian.getDouble("LABORHRS"))*pian.getDouble("QUANTITY")
                #service.error("TEST","X4CAPACITYID in (select x4capacityid from x4capacity where anno='"+str(anno)+"') and destinatario='"+str(titrisWO)+"' and mese='"+str(mese)+"' and craft='"+str(craft)+"' and status='ACCETTATA'")
                for i in range(0,capacitySet.count()):
                    capacity=capacitySet.getMbo(i)
                    totOre=totOre+capacity.getDouble("QTYACC")
                totOre=totOre-totPrestati
                if ((mbo.getDouble("X4ORETRASF")+mbo.getDouble("LABORHRS"))*mbo.getDouble("QUANTITY"))>totOre:
                    service.error("x3terna","ErrorMsg",["Il totale disponibile di ore cedute dal TITRIS selezionato ammonta a "+str(totOre)+"."])
if onupdate or onadd:
    mbo.setValue("X4CHANGEDATE",MXServer.getMXServer().getDate(),11L)