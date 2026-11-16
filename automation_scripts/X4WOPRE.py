from psdi.server import MXServer

def risolviDifformitaWoPrevalso(woPrevalso, wonumPrevalente):

    if woPrevalso is None:
        return

    jobplanSet = None
    x4diffattSet = None
    assetSet = None
    assetAncestorSet = None

    try:
        jobplanSet = woPrevalso.getMboSet("JOBPLAN")

        if jobplanSet.isEmpty():
            return

        jobplanMbo = jobplanSet.getMbo(0)
        x4diffattSet = jobplanMbo.getMboSet("X4DIFFATT")

        if x4diffattSet.isEmpty():
            return

        assetSet = woPrevalso.getMboSet("ASSET")

        if assetSet.isEmpty():
            return

        assetMbo = assetSet.getMbo(0)

        assetAncestorSet = assetMbo.getMboSet(
            "$AANCESTOR_PREV_%s" % woPrevalso.getString("WONUM"),
            "ASSET",
            """assetnum IN (
                SELECT q.assetnum
                FROM asset q
                WHERE (
                    q.ancestor = :assetnum
                    OR (
                        q.parent = :parent
                        AND :classstructureid IN (
                            SELECT classstructureid
                            FROM classancestor
                            WHERE ancestorclassid = 'LINEA'
                            AND classstructureid = :classstructureid
                        )
                    )
                    OR q.assetnum = :assetnum
                )
                AND q.siteid = :siteid

                UNION

                SELECT k.assetnum
                FROM asset k
                WHERE k.assetnum IN (
                    SELECT targetassetnum
                    FROM assetlocrelation p
                    WHERE p.targetassetnum = k.assetnum
                    AND substr(x3sapid, 1, length(x3sapid) - 2) IN (
                        SELECT x3sapid
                        FROM assetlocrelation
                        WHERE targetassetnum = :assetnum
                    )
                )
                AND k.siteid = :siteid
            )"""
        )

        for i in range(x4diffattSet.count()):

            x4diffattMbo = x4diffattSet.getMbo(i)
            idDifformita = x4diffattMbo.getString("DIFFORMITA")

            for j in range(assetAncestorSet.count()):

                assetChildMbo = assetAncestorSet.getMbo(j)
                assetnumChild = assetChildMbo.getString("ASSETNUM")

                x4difforstatSet = woPrevalso.getMboSet(
                    "$RISDIFF_PREV_%s_%s" % (i, j),
                    "X4DIFFORSTAT",
                    "IDDIFFORMITA_EXT='%s' "
                    "AND ASSETNUM='%s' "
                    "AND STATUS IN ('DA VALIDARE', 'VALIDATA', 'SOSPESA')"
                    % (
                        idDifformita.replace("'", "''"),
                        assetnumChild.replace("'", "''")
                    )
                )

                try:
                    for k in range(x4difforstatSet.count()):
                        x4difforstatMbo = x4difforstatSet.getMbo(k)

                        x4difforstatMbo.setValue(
                            "STATUS",
                            "RISOLTA",
                            11L
                        )

                        x4difforstatMbo.setValue(
                            "ODLRISOLUTORE",
                            wonumPrevalente,
                            11L
                        )
                finally:
                    x4difforstatSet.close()

    finally:
        if assetAncestorSet is not None:
            assetAncestorSet.close()

        if assetSet is not None:
            assetSet.close()

        if x4diffattSet is not None:
            x4diffattSet.close()

        if jobplanSet is not None:
            jobplanSet.close()
jpSet=mbo.getMboSet("JOBPLAN")
assetnum=mbo.getString("ASSETNUM")
siteid=mbo.getString("SITEID")
if not jpSet.isEmpty() and mbo.getString("STATUS")=="ESEGUITO" and onupdate:
    datamodello=mbo.getDate("X4DATAMODELLO")
    jpMbo=jpSet.getMbo(0)
    attprevSet=jpMbo.getMboSet("$X4ATTPREVALENTIS$","X4ATTPREVALENTI",":JPNUM=ATTIVITAPREV")
    if not attprevSet.isEmpty():
        for i in range(0,attprevSet.count()):
            attprevMbo=attprevSet.getMbo(i)
            assetSet=mbo.getMboSet("ASSET")
            if not assetSet.isEmpty():
                ancestorSet=MXServer.getMXServer().getMboSet("ASSET", mbo.getUserInfo())
                ancestorSet.setWhere("(assetnum='"+str(assetnum)+"' or parent='"+str(assetnum)+"' or ancestor='"+str(assetnum)+"') and siteid='"+str(siteid)+"'")
                #ancestorSet=mbo.getMboSet("$ASSETANC$","ASSET","(assetnum=:assetnum or parent=:assetnum or ancestor=:assetnum) and siteid=:siteid")
                for j in range(0,ancestorSet.count()):
                    assetnum=ancestorSet.getMbo(j).getString("ASSETNUM")
                    woApertiSet=attprevMbo.getMboSet("$WOPREVALSI$","WORKORDER","JPNUM=:JPNUM and assetnum='"+str(assetnum)+"' and historyflag=0 and x4datamodello is not null")
                    for k in range(0,woApertiSet.count()):
                        woAperto=woApertiSet.getMbo(k)
                        dataPrevalsa=woAperto.getDate("X4DATAMODELLO")
                        if (woAperto.getString("STATUS")=="APERTO" or woAperto.getString("STATUS")=="DA VALIDARE") and datamodello<=dataPrevalsa:
                            #woAperto.changeStatus("ESEGUITO",MXServer.getMXServer().getDate(), "Eseguito EAM per prevalenza")
                            woAperto.setValue("STATUS","NON ESEGUITO",11L)
                            woAperto.setValue("X4NOTE","OdL Prevalso")
                            woAperto.setValue("X4ORDINEPREVALENTE",mbo.getString("WONUM"))
                            woAperto.setValue("HISTORYFLAG",1,11L)
                            risolviDifformitaWoPrevalso(
                                woAperto,
                                mbo.getString("WONUM")
                            )
                    woApertiSet.save()
                    
if (onadd or (onupdate and mbo.isModified("X4DATAMODELLO"))) and mbo.getString("STATUS")=="APERTO" and mbo.getString("X4TIPOODL")=="MANUALE":
    datamodello=mbo.getDate("X4DATAMODELLO")
    jpMbo=jpSet.getMbo(0)
    attprevSet=jpMbo.getMboSet("$X4ATTPREVALENTIS$","X4ATTPREVALENTI",":JPNUM=ATTIVITAPREV")
    if not attprevSet.isEmpty():
        for i in range(0,attprevSet.count()):
            attprevMbo=attprevSet.getMbo(i)
            assetSet=mbo.getMboSet("ASSET")
            if not assetSet.isEmpty():
                ancestorSet=MXServer.getMXServer().getMboSet("ASSET", mbo.getUserInfo())
                ancestorSet.setWhere("(assetnum='"+str(assetnum)+"' or parent='"+str(assetnum)+"' or ancestor='"+str(assetnum)+"') and siteid='"+str(siteid)+"'")
                #ancestorSet=mbo.getMboSet("$ASSETANC$","ASSET","(assetnum=:assetnum or parent=:assetnum or ancestor=:assetnum) and siteid=:siteid")
                for j in range(0,ancestorSet.count()):
                    assetnum=ancestorSet.getMbo(j).getString("ASSETNUM")
                    woApertiSet=attprevMbo.getMboSet("$WOPREVALSI$","WORKORDER","JPNUM=:JPNUM and assetnum='"+str(assetnum)+"' and historyflag=0 and x4datamodello is not null")
                    for k in range(0,woApertiSet.count()):
                        woAperto=woApertiSet.getMbo(k)
                        dataPrevalsa=woAperto.getDate("X4DATAMODELLO")
                        if not woAperto.getString("STATUS")=="ESEGUITO" and not woAperto.getString("STATUS")=="CHIUSO" and datamodello<=dataPrevalsa:
                            #woAperto.changeStatus("ESEGUITO",MXServer.getMXServer().getDate(), "Eseguito EAM per prevalenza")
                            woAperto.setValue("STATUS","NON ESEGUITO",11L)
                            woAperto.setValue("X4NOTE","OdL Prevalso")
                            woAperto.setValue("X4ORDINEPREVALENTE",mbo.getString("WONUM"))
                            woAperto.setValue("HISTORYFLAG",1,11L)
                        elif woAperto.getString("STATUS")=="ESEGUITO" and datamodello<=dataPrevalsa:
                            woAperto.setValue("STATUS","NON ESEGUITO",11L)
                            woAperto.setValue("X4NOTE","OdL Prevalso")
                            woAperto.setValue("X4ORDINEPREVALENTE",mbo.getString("WONUM"))
                            woAperto.setValue("HISTORYFLAG",1,11L)
                    woApertiSet.save()
                    
if (onadd or (onupdate and mbo.isModified("X4DATAMODELLO"))) and mbo.getString("STATUS")=="APERTO":
    datamodello=mbo.getDate("X4DATAMODELLO")
    
    similarWo=mbo.getMboSet("$SIMWO$","WORKORDER",":assetnum=assetnum and jpnum=:jpnum and status='APERTO' and x4datamodello is not null")
    if not similarWo.isEmpty() and not mbo.isNull("X4DATAMODELLO"):
        for f in range(0,similarWo.count()):
            wodanones=similarWo.getMbo(f)
            if datamodello<wodanones.getDate("X4DATAMODELLO"):
                wodanones.setValue("STATUS","NON ESEGUITO",11L)
                wodanones.setValue("X4NOTE","OdL Prevalso")
                wodanones.setValue("X4ORDINEPREVALENTE",mbo.getString("WONUM"))
                wodanones.setValue("HISTORYFLAG",1,11L)
            elif datamodello>wodanones.getDate("X4DATAMODELLO") and mbo.getString("STATUS")!="NON ESEGUITO":
                mbo.setValue("STATUS","NON ESEGUITO",11L)
                mbo.setValue("X4NOTE","OdL Prevalso")
                mbo.setValue("X4ORDINEPREVALENTE",mbo.getString("WONUM"))
                mbo.setValue("HISTORYFLAG",1,11L)
        similarWo.save()