from java.text import SimpleDateFormat
from psdi.util import MXFormat
from java.util import Calendar
from psdi.server import MXServer
import java.util.Date

def get_data_from_mbo(mbo, field):
    """
    Trasforma in string una data ottenuta dal mbo
    """
    date = mbo.getDate(field)
    if date:
        date_format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        date_format.format(date)
        return date
    else:
        return ""

currentUI = mbo.getUserInfo()  # o da service, dipende dal contesto
userId = currentUI.getUserName()      
#if launchPoint=="X4DATEPIANIFICAZIONE" and not mbo.getMboSet("WORKORDER").isEmpty() and mbo.getMboSet("WORKORDER").getMbo(0).getBoolean("X4AGGREGATI")==0:
    #service.error("123", "1")
    #workorderSet = mbo.getMboSet("WORKORDER")
    #if not workorderSet.isEmpty():
        #workorder=workorderSet.getMbo(0)


def toMinute(dateValue):
    if dateValue is None:
        return None

    cal = Calendar.getInstance()
    cal.setTime(dateValue)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.getTime()


if launchPoint == "X4DATEPIANIFICAZIONE" and interactive:
    workorder = mbo.getOwner()
    if workorder.getName().upper() == "WORKORDER":
        if workorder and not workorder.getBoolean("X4AGGREGATI") == True and workorder.getBoolean("X4SERBATOIO") == False:
            dataInizio = toMinute(workorder.getDate("TARGSTARTDATE"))
            dataFine = toMinute(workorder.getDate("TARGCOMPDATE"))
            dataInizioRisorsa = toMinute(mbo.getDate("X4DATAINIZIORISORSA"))
            dataFineRisorsa = toMinute(mbo.getDate("X4DATAFINERISORSA"))
            parent = workorder.getString("PARENT")
            wonum = workorder.getString("WONUM")
            if dataInizio and dataFine and userId != "MAXADMIN":
                if dataFineRisorsa and dataFineRisorsa > dataFine:
                    service.error("x3terna", "ErrorMsg", [
                        "Data di fine risorsa non puo' essere maggiore della data di fine pianificata dell'ordine."
                    ])
                if dataFineRisorsa and dataFineRisorsa < dataInizio:
                    service.error("x3terna", "ErrorMsg", [
                        "Data di fine risorsa non puo' essere minore della data di inizio pianificata dell'ordine."
                    ])
                if dataInizioRisorsa and dataInizioRisorsa < dataInizio:
                    service.error("x3terna", "ErrorMsg", [
                        "Data di inizio risorsa " + str(dataInizioRisorsa) +
                        " non puo' essere minore della data di inizio pianificata " +
                        str(dataInizio) + " dell'ordine."
                    ])
                if dataInizioRisorsa and dataInizioRisorsa > dataFine:
                    service.error("x3terna", "ErrorMsg", [
                        "Data di inizio risorsa non puo' essere maggiore della data di fine pianificata dell'ordine."
                    ])
                if dataInizioRisorsa and dataFineRisorsa and dataInizioRisorsa > dataFineRisorsa:
                    service.error("x3terna", "ErrorMsg", [
                        "Data di inizio risorsa non puo' essere maggiore della data di fine risorsa."
                    ])
                same_targstartdate = False
                same_targcompdate = False
        
                if (
                    dataInizio
                    and dataFine
                    and not mbo.isNull("X4DATAFINERISORSA")
                    and not mbo.isNull("X4DATAINIZIORISORSA")
                    and userId != "MAXADMIN"
                ):
        
                    currentWPLaborUid = mbo.getLong("WPLABORUID")
        
                    # -------------------------------------------------
                    # 1. Controllo SEMPRE la riga corrente usando la mbo
                    #    che contiene i valori aggiornati dal loader/UI
                    # -------------------------------------------------
                    dataInizioR = toMinute(
                        mbo.getDate("X4DATAINIZIORISORSA")
                    )
        
                    dataFineR = toMinute(
                        mbo.getDate("X4DATAFINERISORSA")
                    )
        
                    if dataInizioR == dataInizio:
                        same_targstartdate = True
        
                    if dataFineR == dataFine:
                        same_targcompdate = True
        
                    # -------------------------------------------------
                    # 2. Controllo tutte le ALTRE WPLABOR dell'OdL
                    #    comprese quelle non presenti nel payload
                    # -------------------------------------------------
                    if not interactive:
                        # Set nuovo a ogni WPLABOR: non riuso il WPLABOR set cacheato
                        relName = "$X4CHECKDATE_%s$" % str(currentWPLaborUid)
                    
                        wpLaborSet = workorder.getMboSet(
                            relName,
                            "WPLABOR",
                            "wonum=:wonum and siteid=:siteid"
                        )
                    
                        wpLaborSet.reset()
                    else:
                        # Da UI uso il set in memoria, che contiene le modifiche dell'utente
                        wpLaborSet = workorder.getMboSet("WPLABOR")
        
                    for i in range(0, wpLaborSet.count()):
        
                        wpLabor = wpLaborSet.getMbo(i)
        
                        # La riga corrente è già stata controllata sopra
                        # usando la mbo aggiornata.
                        # Nel WPLABOR set potrebbe ancora avere i valori DB vecchi.
                        if (
                            wpLabor.getLong("WPLABORUID")
                            == currentWPLaborUid
                        ):
                            continue
        
                        x4_data_inizio_risorsa = toMinute(
                            wpLabor.getDate("X4DATAINIZIORISORSA")
                        )
        
                        x4_data_fine_risorsa = toMinute(
                            wpLabor.getDate("X4DATAFINERISORSA")
                        )
        
                        if x4_data_inizio_risorsa == dataInizio:
                            same_targstartdate = True
        
                        if x4_data_fine_risorsa == dataFine:
                            same_targcompdate = True
        
                        # Ho già trovato entrambe le coincidenze:
                        # inutile continuare il ciclo
                        if same_targstartdate and same_targcompdate:
                            break
        
                    if not same_targstartdate:
                        service.error(
                            "x3terna",
                            "ErrorMsg",
                            [
                                "Controllare che ci sia almeno una data di inizio risorsa "
                                "che coincida con la data di inizio pianificata dell'OdL"
                            ]
                        )
        
                    if not same_targcompdate:
                        service.error(
                            "x3terna",
                            "ErrorMsg",
                            [
                                "Controllare che ci sia almeno una data di fine risorsa "
                                "che coincida con la data di fine pianificata dell'OdL"
                            ]
                        )
                
                
            # Valorizzazione campo X4ORETRANS di WPLABOR dato l'ownergroup dell'odl padre e x3sapid dell'asset padre 
            if wonum and onadd or (onupdate and  mbo.isModified("X4TITRIS") and mbo.isNull("X4ORETRASF")):
                # Recupero l'ordine di lavoro padre (WORKORDER)
                #wo_parent_set = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
                #wo_parent_set.setWhere("WONUM='{}'".format(wonum))
                #wo_parent_set.reset()
                
                #if not wo_parent_set.isEmpty():
                    #wo_parent = wo_parent_set.getMbo(0)
                wo_parent = mbo.getOwner()
                if wo_parent:
                    ownergroup = mbo.getString("X4TITRIS")
                    assetnum = wo_parent.getString("ASSETNUM")
                    x3sapid=wo_parent.getString("X4SAPIDASSET")
                    '''
                    if assetnum:
                        # Recupero l'asset corrente
                        asset_set = MXServer.getMXServer().getMboSet("ASSET", mbo.getUserInfo())
                        asset_set.setWhere("ASSETNUM='{}'".format(assetnum))
                        asset_set.reset()
                        asset = asset_set.getMbo(0)
                        parent_assetnum = asset.getString("PARENT")
                        
                        if parent_assetnum:
                            # Recupero l'asset padre
                            asset_parent_set = MXServer.getMXServer().getMboSet("ASSET", mbo.getUserInfo())
                            asset_parent_set.setWhere("ASSETNUM='{}'".format(parent_assetnum))
                            asset_parent_set.reset()
                            asset_parent = asset_parent_set.getMbo(0)
                            x3sapid = asset_parent.getString("X3SAPID")
                    '''
                    
                    # Query sulla tabella X4MATRICEDEST
                    if ownergroup and x3sapid:
                        matrice_set = MXServer.getMXServer().getMboSet("X4MATRICEDEST", mbo.getUserInfo())
                        matrice_set.setWhere("DESTINAZIONE='{}' AND ORIGINE = '{}'".format(x3sapid[:7], ownergroup))
                        matrice_set.reset()
                        if not matrice_set.isEmpty():
                            matrice = matrice_set.getMbo(0)
                            ttrasf = matrice.getDouble("TTRASF")
                            
                               
                            if ttrasf and mbo.isNull("X4ORETRASF"):
                                mbo.setValue("X4ORETRASF", ttrasf, 11L)
                                if onadd:
                                    wo_parent.setValue("X4TTRASF",wo_parent.getDouble("X4TTRASF")+ttrasf)
                                    #mbo.getThisMboSet().save()
                                
                            
                        matrice_set.close()
                        #asset_parent_set.close()
                        #asset_set.close()
                #wo_parent_set.close()
                
                #Set CMO
                titris=mbo.getString("X4TITRIS")
                checkCMO=mbo.getMboSet("$wpla2b$","X4CRAFTPERYEAR","CRAFT =:CRAFT AND :X4TITRIS =UDB AND EXTRACT(YEAR FROM :X4DATAFINERISORSA)=ANNO")
                if not checkCMO.isEmpty() and mbo.isNull("RATE"):
                    checkCMOmbo=checkCMO.getMbo(0)
                    rate=checkCMOmbo.getDouble("PAYRATE")
                    mbo.setValue("RATE",rate)
                    #mbo.getThisMboSet().save
                
                
                # wpLaborSet1=workorder.getMboSet("$WPLABOR1$","WPLABOR","WONUM=:WONUM and SITEID=:SITEID and X4DATAINIZIORISORSA=:TARGSTARTDATE")
                # wpLaborSet2=workorder.getMboSet("$WPLABOR2$","WPLABOR","WONUM=:WONUM and SITEID=:SITEID and X4DATAFINERISORSA=:TARGCOMPDATE")
                # if wpLaborSet1.isEmpty() or wpLaborSet2.isEmpty():
                #     service.error("x3terna", "ErrorMsg", ["Controllare che ci sia almeno una data di inizio/fine risorsa che coincida con la data di inizio/fine pianificata dellOdL"])
                
if ondelete and interactive and not mbo.isNull("X4DATAINIZIORISORSA") and not mbo.isNull("X4DATAFINERISORSA") and mbo.getMboSet("WORKORDER").getMbo(0).getInt("X4AGGREGATI")==0 and userId!="MAXADMIN":
    same_targstartdatedel = list()
    same_targcompdatedel = list()
    workorderSet = mbo.getMboSet("WORKORDER")
    if not workorderSet.isEmpty():
        workorder=workorderSet.getMbo(0)
        dataInizio = workorder.getDate("TARGSTARTDATE")
        dataFine = workorder.getDate("TARGCOMPDATE")
        altriWPLABORSet=mbo.getMboSet("$ALTRIWPLAB$","WPLABOR","WONUM=:wonum and wplaborid!=:wplaborid")
        if not altriWPLABORSet.isEmpty():
            for i in range(0, altriWPLABORSet.count()):
                altriWPLABOR=altriWPLABORSet.getMbo(i)
                if MXFormat.dateTimeToSQLString(get_data_from_mbo(workorder, "TARGSTARTDATE")) == MXFormat.dateTimeToSQLString(get_data_from_mbo(altriWPLABOR, "X4DATAINIZIORISORSA")):
                    same_targstartdatedel.append(True)
                if MXFormat.dateTimeToSQLString(get_data_from_mbo(workorder, "TARGCOMPDATE")) == MXFormat.dateTimeToSQLString(get_data_from_mbo(altriWPLABOR, "X4DATAFINERISORSA")):
                    same_targcompdatedel.append(True)
                if True not in same_targstartdatedel:
                    #service.error("TEST",workorder.getString("WONUM"))
                    service.error("x3terna", "ErrorMsg", ["Controllare che ci sia almeno una data di inizio risorsa che coincida con la data di inizio/fine pianificata dell'OdL"])
                if True not in same_targcompdatedel:
                    service.error("x3terna", "ErrorMsg", ["Controllare che ci sia almeno una data di fine risorsa che coincida con la data di fine pianificata dell'OdL"])