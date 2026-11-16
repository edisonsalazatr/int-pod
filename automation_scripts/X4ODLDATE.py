from java.util import Date
from java.util import Calendar
from psdi.server import MXServer


finiscinondopo=mbo.getDate("FNLCONSTRAINT")
inizianonprima=mbo.getDate("SNECONSTRAINT")
fine=mbo.getDate("TARGCOMPDATE")
inizio=mbo.getDate("TARGSTARTDATE")
if inizianonprima and inizio:
    if inizio<inizianonprima:
        service.error("x3terna", "ErrorMsg", ["Valore di data di inizio non ammissibile in quanto minore della data Inizia non prima di"])
if finiscinondopo and fine:
    if fine>finiscinondopo:
        service.error("x3terna", "ErrorMsg", ["Valore di data di fine non ammissibile in quanto maggiore della data di massimo posticipo"])
if finiscinondopo and (finiscinondopo < fine):
    service.error("x3terna", "ErrorMsg", ["Si prega di inserire la Data di massimo posticipo superiore/maggiore la Data pianificata fine."])
    
        
app = None
wcs = service.webclientsession()
if wcs:
    app = wcs.getCurrentApp()

if str(app) == "AppInstance [id: wotrack_d]":

    wonum=mbo.getString("WONUM")
    siteid=mbo.getString("SITEID")    
        
    finiscinondopo=mbo.getDate("FNLCONSTRAINT")
    inizianonprima=mbo.getDate("SNECONSTRAINT")
    fine=mbo.getDate("TARGCOMPDATE")
    inizio=mbo.getDate("TARGSTARTDATE")
    if inizianonprima and inizio:
        if inizio<inizianonprima:
            service.error("x3terna", "ErrorMsg", ["Valore di data di inizio non ammissibile in quanto minore della data Inizia non prima di"])
    if finiscinondopo and fine:
        if fine>finiscinondopo:
            service.error("x3terna", "ErrorMsg", ["Valore di data di fine non ammissibile in quanto maggiore della data di massimo posticipo"])
    if finiscinondopo and (finiscinondopo < fine):
        service.error("x3terna", "ErrorMsg", ["Si prega di inserire la Data di massimo posticipo superiore/maggiore la Data pianificata fine."])
    
    '''
    if not mbo.isNull("TARGCOMPDATE") and (mbo.isNull("TARGSTARTDATE") or mbo.isModified("ESTDUR") or mbo.isModified("TARGCOMPDATE")) and mbo.getBoolean("X4SERBATOIO")==False and launchPoint =="X4ODLDATE_DATE" and (mbo.getString("STATUS")=="APERTO" or mbo.getString("STATUS")=="DA VALIDARE" or mbo.getString("STATUS")=="SCHEDULABILE" or mbo.getString("STATUS")=="SCHEDULATO"):
        durata=mbo.getInt("ESTDUR")
        if not durata:
            pdlSet=mbo.getMboSet("JOBPLAN")
            if not pdlSet.isEmpty():
                pdlMbo=pdlSet.getMbo(0)
                durata=pdlMbo.getInt("JPDURATION")
        datafine=mbo.getDate("TARGCOMPDATE")
        datainizio=mbo.getDate("TARGSTARTDATE")
        if not mbo.isNull("TARGCOMPDATE"):
            cal = Calendar.getInstance()
            cal.setTime(datafine)
            cal.add(Calendar.HOUR, -durata)
            #service.error("TEST",str(durata))
            newDate = cal.getTime()
            #service.error("TEST",str(newDate))
            mbo.setValue("TARGSTARTDATE",newDate)
    '''
    #service.error("TEST","agg:"+str(mbo.getBoolean("X4AGGREGATI"))+", ")
    if mbo.isModified("TARGCOMPDATE") and mbo.getBoolean("X4AGGREGATI")==True  and mbo.getBoolean("HASCHILDREN")==True and not mbo.isNull("TARGCOMPDATE") and not mbo.isNull("TARGSTARTDATE") and mbo.getString("STATUS") in ("APERTO","DA VALIDARE") and mbo.isNull("WOJP2") and not mbo.isModified("WOJP2"):
        mboValue = mbo.getMboValue("TARGCOMPDATE")
        prevTarg= mboValue.getInitialValue().asDate()
        differenza_millis = fine.getTime() - prevTarg.getTime()
        differenza_giorni = differenza_millis / 60000
        inizioPadre=mbo.getDate("TARGSTARTDATE")
        if not mbo.isNull("TARGSTARTDATE"):
            cal4 = Calendar.getInstance()
            cal4.setTime(inizioPadre)
            cal4.add(Calendar.MINUTE, int(differenza_giorni))
            data_calcolata4 = cal4.getTime()
            if data_calcolata4<inizianonprima:
                service.error("x3terna", "ErrorMsg", ["Valore di data di inizio non ammissibile in quanto minore della data Inizia non prima di"])
            #mbo.getThisMboSet().save()
            wochildrenSet=mbo.getMboSet("CHILDNOTASK")
            if not wochildrenSet.isEmpty():
                for i in range (0,wochildrenSet.count()):
                    wochildrenMbo=wochildrenSet.getMbo(i)
                    datainizio=wochildrenMbo.getDate("TARGSTARTDATE")
                    datafine=wochildrenMbo.getDate("TARGCOMPDATE")
                    if datafine and datainizio:
                        cal1 = Calendar.getInstance()
                        cal1.setTime(datainizio)
                        cal1.add(Calendar.MINUTE, int(differenza_giorni))
                        data_calcolata1 = cal1.getTime()
                        wochildrenMbo.setValue("TARGSTARTDATE",data_calcolata1,11L)
                        cal2 = Calendar.getInstance()
                        cal2.setTime(datafine)
                        cal2.add(Calendar.MINUTE, int(differenza_giorni))
                        data_calcolata2 = cal2.getTime()
                        wochildrenMbo.setValue("TARGCOMPDATE",data_calcolata2,11L)
                        
                wochildrenSet.save()
                sameWo = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
                sameWo.setWhere("WONUM='%s' and siteid='%s'" % (wonum, siteid))
                sameWo.reset()
                sameWoMbo = sameWo.getMbo(0)
                sameWoMbo.setValue("TARGSTARTDATE",data_calcolata4,11L)
                sameWo.save()
                sameWo.close()
            wochildrenSet.close()
    #mbo.getThisMboSet().save()
    
    if mbo.getString("WOJP2")=="1":
        mbo.setValue("WOJP1","0",11L)