from psdi.mbo import MboConstants
from java.text import SimpleDateFormat
from java.util import Date

def beforeMboData(ctx):
    if(ctx.getProcessTable()=="WORKORDER"):
        wonum=ctx.getData().getCurrentData("WONUM")
        siteid=ctx.getData().getCurrentData("SITEID")
        description=ctx.getData().getCurrentData("DESCRIPTION")
        x4aggregati=ctx.getData().getCurrentData("X4AGGREGATI")
        status=ctx.getData().getCurrentData("STATUS")
        assetnum=ctx.getData().getCurrentData("ASSETNUM")
        jpnum=ctx.getData().getCurrentData("JPNUM")
        x4fisicoplan=ctx.getData().getCurrentData("X4FISICOPLAN")
        x4fisico=ctx.getData().getCurrentData("X4FISICO")
        ownergroup=ctx.getData().getCurrentData("OWNERGROUP")
        x4tipospesa=ctx.getData().getCurrentData("X4TIPOSPESA")
        coin=ctx.getData().getCurrentData("X4COIN")
        x4burks=ctx.getData().getCurrentData("X4BURKS")
        x4perimetroiint=ctx.getData().getCurrentData("X4PERIMETROINT")
        x4tipoodl=ctx.getData().getCurrentData("X4TIPOODL")
        x4sapidasset=ctx.getData().getCurrentData("X4SAPIDASSET")
        woeq9=ctx.getData().getCurrentData("WOEQ9")
        targstartdate=ctx.getData().getCurrentData("TARGSTARTDATE")
        targcompdate=ctx.getData().getCurrentData("TARGCOMPDATE")
        actfinish=ctx.getData().getCurrentData("ACTFINISH")
        sneconstraint=ctx.getData().getCurrentData("SNECONSTRAINT")
        fnlconstraint=ctx.getData().getCurrentData("FNLCONSTRAINT")
        estdur=ctx.getData().getCurrentData("ESTDUR")

        oi=ctx.getData().getCurrentData("X4NORDINEINTERNO")
        wbe=ctx.getData().getCurrentData("X4WBE")
        wbs=ctx.getData().getCurrentData("X4WBS")
        odm=ctx.getData().getCurrentData("X4ODM")
        ntk=ctx.getData().getCurrentData("X4NTK")
        opntk=ctx.getData().getCurrentData("X4NOPERAZIONE")
        wbepian=ctx.getData().getCurrentData("X4WBEPIAN")
        wbspian=ctx.getData().getCurrentData("X4WBSPIAN")

        Wmbo = ctx.getMbo()

        if wonum is None or str(wonum).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare WONUM."])

        if siteid is None or str(siteid).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare SITEID."])

        if description is None or str(description).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare DESCRIPTION."])

        if x4aggregati is None or str(x4aggregati).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare X4AGGREGATI."])

        if status is None or str(status).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare STATUS."])

        if assetnum is None or str(assetnum).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare ASSETNUM."])

        if jpnum is None or str(jpnum).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare JPNUM."])

        if x4fisicoplan is None or str(x4fisicoplan).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare X4FISICOPLAN."])

        if x4fisico is None or str(x4fisico).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare X4FISICO."])

        if ownergroup is None or str(ownergroup).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare OWNERGROUP."])

        if x4tipospesa is None or str(x4tipospesa).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare X4TIPOSPESA."])

        if coin is None or str(coin).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare X4COIN."])

        if x4burks is None or str(x4burks).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare X4BURKS."])

        if x4perimetroiint is None or str(x4perimetroiint).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare X4PERIMETROINT."])

        if x4tipoodl is None or str(x4tipoodl).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare X4TIPOODL."])

        if x4sapidasset is None or str(x4sapidasset).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare X4SAPIDASSET."])

        if woeq9 is None or str(woeq9).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare WOEQ9."])

        if targstartdate is None or str(targstartdate).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare TARGSTARTDATE."])

        if targcompdate is None or str(targcompdate).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare TARGCOMPDATE."])

        if actfinish is None or str(actfinish).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare ACTFINISH."])

        if sneconstraint is None or str(sneconstraint).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare SNECONSTRAINT."])

        if fnlconstraint is None or str(fnlconstraint).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare FNLCONSTRAINT."])

        if estdur is None or str(estdur).strip() == "":
            ctx.error("x3terna", "ErrorMsg", ["E' obbligatorio indicare ESTDUR."])


        if coin:
            Wmbo.setValue("X4COIN", coin, MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4COIN")

        if oi:
            Wmbo.setValue("X4NORDINEINTERNO", oi, MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4NORDINEINTERNO")

        if wbe:
            Wmbo.setValue("X4WBE", wbe, MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4WBE")

        if wbs:
            Wmbo.setValue("X4WBS", wbs, MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4WBS")

        if odm:
            Wmbo.setValue("X4ODM", odm, MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4ODM")

        if ntk:
            Wmbo.setValue("X4NTK", ntk, MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4NTK")

        if opntk:
            Wmbo.setValue("X4NOPERAZIONE", opntk, MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4NOPERAZIONE")

        if wbepian:
            Wmbo.setValue("X4WBEPIAN", wbepian, MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4WBEPIAN")

        if wbspian:
            Wmbo.setValue("X4WBSPIAN", wbspian, MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4WBSPIAN")

        if x4burks:
            Wmbo.setValue("X4BURKS", x4burks, MboConstants.NOVALIDATION_AND_NOACTION)

    if(ctx.getProcessTable()=="X4DIFFOR_WO"):
        assetnum=ctx.getData().getCurrentData("ASSETNUM")
        iddiff=ctx.getData().getCurrentData("IDDIFFORMITA")
        Wmbo = ctx.getMbo();
        if assetnum:
            Wmbo.setValue("ASSETNUM",assetnum,MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("ASSETNUM",MboConstants.NOVALIDATION_AND_NOACTION)
        if iddiff:
            checkExists=Wmbo.getMboSet("$CATDIFF$","X4CATDIFFOR","iddifformita='"+str(iddiff)+"'")
            if checkExists.isEmpty():
                ctx.error("x3terna", "ErrorMsg", ["Difformità non esistente."])
            else:
                Wmbo.setValue("IDDIFFORMITA",iddiff,MboConstants.NOVALIDATION_AND_NOACTION)
    if(ctx.getProcessTable()=="X4INTERF_WO"):
        assetnum=ctx.getData().getCurrentData("ASSETNUM")
        idint=ctx.getData().getCurrentData("IDINTERFERENZA")
        Wmbo = ctx.getMbo();
        if assetnum:
            Wmbo.setValue("ASSETNUM",assetnum,MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("ASSETNUM",MboConstants.NOVALIDATION_AND_NOACTION)
        if idint:
            Wmbo.setValue("IDINTERFERENZA",idint,MboConstants.NOVALIDATION_AND_NOACTION)
    if(ctx.getProcessTable()=="X4MISUREWO"):
        assetnum=ctx.getData().getCurrentData("ASSETNUM")
        Wmbo = ctx.getMbo();
        if assetnum:
            Wmbo.setValue("ASSETNUM",assetnum,MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("ASSETNUM",MboConstants.NOVALIDATION_AND_NOACTION)
            
    if(ctx.getProcessTable()=="WPLABOR"):
        titris=ctx.getData().getCurrentData("X4TITRIS")
        Wmbo = ctx.getMbo();
        if titris:
            Wmbo.setValue("X4TITRIS",titris,MboConstants.NOVALIDATION_AND_NOACTION)
        else:
            Wmbo.setValueNull("X4TITRIS",MboConstants.NOVALIDATION_AND_NOACTION)
    '''        
    if(ctx.getProcessTable()=="LABTRANS"):
        Wmbo = ctx.getMbo()
        #ctx.getData().setCurrentData("GENAPPRSERVRECEIPT","1")
        #Wmbo.setValue("GENAPPRSERVRECEIPT","1",11L)
        datainizio_str = ctx.getData().getCurrentData("STARTDATETIME")
        if datainizio_str:
            datainizio_part, timeinizio_part = datainizio_str.split("T")
            Wmbo.setValue("STARTDATETIME", datainizio_str, 11L)
            Wmbo.setValue("STARTDATE", datainizio_part, 11L)
            Wmbo.setValue("STARTTIME", timeinizio_part, 11L)
            
        datafine_str = ctx.getData().getCurrentData("FINISHDATETIME")
        if datafine_str:
            datafine_part, timefine_part = datafine_str.split("T")
            Wmbo.setValue("FINISHDATETIME", datafine_str, 11L)
            Wmbo.setValue("FINISHDATE", datafine_part, 11L)
            Wmbo.setValue("FINISHTIME", timefine_part, 11L)
            
        durata = ctx.getData().getCurrentData("regularhrs")
        if durata:
            Wmbo.setValue("regularhrs", durata, 11L)
            
        duratauff = ctx.getData().getCurrentData("x4oreuff")
        if durata:
            Wmbo.setValue("x4oreuff", duratauff, 11L)
            
        duratatrasf = ctx.getData().getCurrentData("x4oretrasf")
        if durata:
            Wmbo.setValue("x4oretrasf", duratatrasf, 11L)
        tot=float(durata)+float(duratauff)+float(duratatrasf)
        Wmbo.setValue("X4ORETOT",tot)