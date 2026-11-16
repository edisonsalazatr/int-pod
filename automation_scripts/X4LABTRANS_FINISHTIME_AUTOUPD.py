'''
from java.util import Calendar
if interactive and onadd:
    STARTTIME = mbo.getDate("STARTTIME")
    REGULARHRS = mbo.getDouble("REGULARHRS")
    X4ORETRASF = mbo.getDouble("X4ORETRASF")
    X4OREUFF = mbo.getDouble("X4OREUFF")
    
    if not STARTTIME:
        service.error("x3terna", "ErrorMsg", ["Il campo STARTTIME e' obbligatorio."])
    
    sum_of_hours = sum([int(i * 60) for i in [REGULARHRS, X4ORETRASF, X4OREUFF] if i])
    
    calendar = Calendar.getInstance()
    calendar.setTime(STARTTIME)
    calendar.add(Calendar.MINUTE, sum_of_hours)
    finishtime = calendar.getTime()
    mbo.setValue("FINISHTIME", finishtime)
'''   
if onadd:
    workorder = mbo.getMboSet("WORKORDER").getMbo(0)
    
    if workorder and workorder.getString("STATUS") in ('ESEGUIBILE', 'SCHEDULATO', 'ESEGUITO PARZ.'):
        workorder.setModified(True)


'''
wo = mbo.getMboSet("WORKORDER").getMbo(0)
if wo is not None and onadd:
    stato = wo.getString("STATUS")
    if stato == "ESEGUITO":
        #errorkey = "inserimentoNonConsentito"
       # params = ["Non è possibile inserire consuntivi su un ODL in stato ESEGUITO."]
        service.error("x3terna", "ErrorMsg", ["Non è possibile inserire consuntivi su un ODL in stato ESEGUITO."])