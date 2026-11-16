from java.text import SimpleDateFormat
from java.util import Calendar
from psdi.util.logging import MXLoggerFactory
from psdi.server import MXServer
from psdi.iface.mic import PublishChannelCache

logger = MXLoggerFactory.getLogger("maximo.mxdev")

# service.error("test", "click")
mboKey = mbo.getLong("WORKORDERID")  # Salva la chiave primaria (ad esempio, ASSETNUM)
if not mbo.getString("STATUS") in ("APERTO","DA VALIDARE"):
    service.error("x3terna", "ErrorMsg", ["OdL in stato non ammissibile."])


wo = mbo

wo.setValue("WOJP2","INPRG")
wo_children = wo.getMboSet("CHILDNOTASK")
wo_children_mbo = [wo_children.getMbo(i) for i in range(0, wo_children.count())]

logger.debug("OdL ha %s figli" % len(wo_children_mbo))
dateinizio=[]
datefine=[]

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


def datestr_to_obj(date):
    """
    Trasforma una string in un oggetto Jython
    """
    calendar = Calendar.getInstance()
    calendar.setTime(date)
    return calendar.getTime()


def find_date_diff(date_2, date_1):
    """
    Trova la differenza in giorni fra due date
    """
    #date_1 = datestr_to_obj(date_1)
    #date_2 = datestr_to_obj(date_2)
    day_ms = 24 * 60 * 60 * 1000
    date_1_calendar = Calendar.getInstance()
    date_1_calendar.setTime(date_1)
    date_2_calendar = Calendar.getInstance()
    date_2_calendar.setTime(date_2)
    diff_in_ms = date_1_calendar.getTimeInMillis() - date_2_calendar.getTimeInMillis()
    res =  diff_in_ms / (1000 * 60)
    return res
    #diff_in_ms = abs(date_1_calendar.getTimeInMillis() - date_2_calendar.getTimeInMillis())
    #return diff_in_ms // day_ms
    
def get_estdur(mbo):
    estdur = mbo.getDouble("ESTDUR")
    estdur = round(estdur * 60) if estdur else 0
    #service.error("TEST",str(estdur))
    return int(estdur)


mbo_propedeutico = {mbo.getInt("WOEQ11"): mbo for mbo in wo_children_mbo \
    if mbo.getInt("WOEQ11")}
wo_children_senza_prop = [mbo for mbo in wo_children_mbo if not mbo.getInt("WOEQ11")]
wo_children_solo_prop = [mbo for mbo in wo_children_mbo if mbo.getInt("WOEQ11") in mbo_propedeutico]


woprp_with_none = list()
same_worpr = dict()
others = list()
# for odl in wo_children_solo_prop:
#     woprp = odl.getInt("WOEQ11")
#     if not woprp:
#         woprp_with_none.append(odl)
        
for odl in wo_children_solo_prop:
    if odl in woprp_with_none:
        continue
    # wonum = odl.getInt("WONUM")
    woprp = odl.getInt("WOEQ11")
    if woprp not in same_worpr:
        same_worpr[woprp] = list()
    same_worpr[woprp].append(odl)
        
for woprp, odl_list in same_worpr.copy().items():
    if len(odl_list) < 2:
        others.append(odl_list)
        del same_worpr[woprp]
        
# service.error("11", str(same_worpr.items()) +"  - "+ str(others))


wo_children_solo_prop = sum(list(same_worpr.values()) + others, list())

# service.error("11", str(wo_children_solo_prop))
# service.error("12", str([odl.getInt("WONUM") for odl in wo_children_solo_prop]))

# service.error("q", str([[m.getDate("TARGCOMPDATE"), m.getDate("TARGSTARTDATE")] for m in wo_children_senza_prop]))

logger.debug("OdL ha %s figli con propedeuticita" % len(wo_children_solo_prop))

# IMPOSTARE DUE VARIABILI "LIMITE" (inizio_max e fine_min)
#######
#  1  #
#######
inizio_max = [get_data_from_mbo(mbo, "SNECONSTRAINT") for mbo in wo_children_mbo if mbo.getDate("SNECONSTRAINT")]
inizio_max = datestr_to_obj(max(inizio_max)) if inizio_max else None
#######
#  2  #
#######
fine_min = [get_data_from_mbo(mbo, "FNLCONSTRAINT") for mbo in wo_children_mbo if mbo.getDate("FNLCONSTRAINT")]
fine_min = datestr_to_obj(min(fine_min)) if fine_min else None
durata_max = [get_estdur(mbo) for mbo in wo_children_mbo if mbo.getDouble("ESTDUR") and mbo.isNull("WOEQ11")]
#service.error("TEST",str(durata_max))

durata_max = max(durata_max) if durata_max else None
durata_max_senza_prop = [get_estdur(mbo) for mbo in wo_children_mbo if mbo.getFloat("ESTDUR") and mbo.isNull("WOEQ11")]
durata_max_senza_prop = max(durata_max_senza_prop) if durata_max_senza_prop else None

targcompdate_min = [get_data_from_mbo(mbo, "TARGCOMPDATE") for mbo in wo_children_mbo if mbo.getDate("TARGCOMPDATE")]

targcompdate_min = datestr_to_obj(min(targcompdate_min)) if targcompdate_min else None

targstartdate_min = [get_data_from_mbo(mbo, "TARGSTARTDATE") for mbo in wo_children_mbo if mbo.getDate("TARGSTARTDATE")]


# service.error("2", str([(get_data_from_mbo(mbo, "TARGSTARTDATE"), get_data_from_mbo(mbo, "SNECONSTRAINT"), get_data_from_mbo(mbo, "FNLCONSTRAINT")) for mbo in wo_children_mbo if mbo.getDate("TARGSTARTDATE")]))


targstartdate_min = datestr_to_obj(min(targstartdate_min)) if targstartdate_min else None






ordered_by_propedeutico = list()
parent_DURATA = None
parent_TARGSTARTDATE = None
parent_TARGCOMPDATE = None

#######
#  3  #
#######
if mbo_propedeutico:
    logger.debug("Ci sono dei mbo con propedeutico, prendo quello con durata massima")
    
    # before (21/02):
    # propedeutico_max_estdur = [estdur(mbo) for wonum, mbo in mbo_propedeutico.items() if mbo.getFloat("ESTDUR")]
    # propedeutico_max_estdur = max(propedeutico_max_estdur) if propedeutico_max_estdur else None
    
    # after (21/02): 
    _tmp_data = dict()
    for wonum, odl in mbo_propedeutico.items():
        if odl:
            estdur = get_estdur(odl)
            WOEQ11 = odl.getInt("WOEQ11")
            checkPropSet=wo.getMboSet("$CHECKFIGLI$","WORKORDER","parent=:wonum and siteid=:siteid and wonum='"+str(WOEQ11)+"'")
            if checkPropSet.isEmpty():
                service.error("x3terna", "ErrorMsg", ["Si prega di valorizzare correttamente la colonna Predecessori."])
            if WOEQ11 not in _tmp_data:
                _tmp_data[WOEQ11] = dict()
                _tmp_data[WOEQ11]["estdur"] = estdur
                continue
            if _tmp_data[WOEQ11]["estdur"] < estdur:
                _tmp_data[WOEQ11]["estdur"] = estdur
    propedeutico_max_estdur = sum([i["estdur"] for i in _tmp_data.values()]) if _tmp_data else 0
    #service.error("riga 141, propedeutico_max_estdur--", str(propedeutico_max_estdur))
    
    # se c'è propedeuticità, la durata sarà uguale a: 
    # il MAX di quelli senza propedeuticità + la durata degli odl propedeutici
    #######
    #  5  #
    #######
    if not durata_max:
        service.error("x3terna", "ErrorMsg", ["Si prega di verificare i predecessori."])
    parent_DURATA = durata_max + propedeutico_max_estdur
    if propedeutico_max_estdur:
        logger.debug("Aggiungo alla durata_max %s quella propedeutico_max_estdur %s" % (durata_max, propedeutico_max_estdur))
else:
    # la durata del padre se non c'è propedeuticità sarà uguale alla MAX durata dei figli
    #######
    #  4  #
    #######
    parent_DURATA = durata_max
    logger.debug("Non ci sono mbo con propedeuticità.")
logger.debug("parent_DURATA = %s" % parent_DURATA)
#result=float(parent_DURATA)/float(60)
#result="{:.2f}".format(result)
#service.error("TEST",str(result))
wo.setValue("ESTDUR", float(parent_DURATA)/float(60))
logger.debug("Imposto parent_DURATA %s al campo ESTDUR" % parent_DURATA)

#######
#  7  #
#######
datainiziotarget = targstartdate_min
calIni = Calendar.getInstance()
calIni.setTime(datainiziotarget)
calIni.add(Calendar.MINUTE, parent_DURATA)
datafinetarget = calIni.getTime()

logger.debug("inizio_max = %s" % inizio_max)
logger.debug("fine_min = %s" % fine_min)
logger.debug("durata_max = %s" % durata_max)
logger.debug("durata_max_senza_prop = %s" % durata_max_senza_prop)
logger.debug("targcompdate_min = %s" % targcompdate_min)
logger.debug("datafinetarget = %s" % datafinetarget)

if not datafinetarget:
    service.error("x3terna", "ErrorMsg", ["Calcolo impossibile"])

if not fine_min:
    service.error("x3terna", "ErrorMsg", ["Calcolo impossibile: il campo Data massimo posticipo non ha valore: %s %s" % (fine_min, inizio_max)])

if not inizio_max:
    service.error("x3terna", "ErrorMsg", ["Calcolo impossibile: il campo -Inizia non prima di- non ha valore."])
    
# ################################################
# 6     PRIMO GIRO DI IMPOSTAZIONE DATE DEL PADRE#
# ################################################

# prendo la data MINIMA di TARGCOMPDATE (chiamimola DATAFINETARGET) dei figli e..
# la pongo come data obiettivo di TARGCOMPDATE del padre

if datafinetarget < fine_min:
    logger.debug("Entrato in datafinetarget < fine_min, imposto datafinetarget %s al campo TARGCOMPDATE" % datafinetarget)
    #######
    #  8  #
    #######
    #Commento 17/03
    #wo.setValue("TARGCOMPDATE", datafinetarget, 11L)   # Sat Oct 05 00:00:00 CEST 2024
else:
    logger.debug("Trovo la differenza tra fine_min - datafinetarget")
    data_diff = find_date_diff(fine_min, datafinetarget)
    #service.error("c", str(data_diff))
    
    cal4 = Calendar.getInstance()
    cal4.setTime(datafinetarget)
    cal4.add(Calendar.MINUTE, data_diff)
    datafinetarget = cal4.getTime()
    logger.debug("sposto di x giorni (la differenza: %s) la datafinetarget a sinistra (sottraggo)" % data_diff)
    #Commento 17/03
    #wo.setValue("TARGCOMPDATE", datafinetarget, 11L)
    
    logger.debug("Imposto datafinetarget %s al campo TARGCOMPDATE" % datafinetarget)



#######
#  9  #
#######

# service.error("1", str(parent_DURATA) )

cal = Calendar.getInstance()
cal.setTime(datafinetarget)
cal.add(Calendar.MINUTE, -parent_DURATA)
datafinetargetless_durata = cal.getTime()

logger.debug("Sottratto le ore %s di parent_DURATA da datafinetarget con il risultato: %s" % (parent_DURATA, datafinetargetless_durata))
inizi_solo_prop = list() # inserito da wo_children_solo_prop (sotto)
inizi_tutti_figli = list() # inserito da wo_children_mbo (sotto)
update_prop=[]
#Stesso calcolo ma per datafinetargetless_durata
if datafinetargetless_durata > inizio_max:
    logger.debug("Entrato in datafinetarget < fine_min, imposto datafinetarget %s al campo TARGCOMPDATE" % datafinetarget)
    #######
    #  8.2  #
    #######
    #Commento 17/03
    #wo.setValue("TARGCOMPDATE", datafinetarget, 11L)   # Sat Oct 05 00:00:00 CEST 2024
else:
    logger.debug("Trovo la differenza tra fine_min - datafinetarget")
    #service.error("TEST",str(datafinetargetless_durata))
    data_diff = find_date_diff(datafinetargetless_durata, inizio_max)
    #service.error("c", str(data_diff))
    
    cal42 = Calendar.getInstance()
    cal42.setTime(datafinetargetless_durata)
    cal42.add(Calendar.MINUTE, data_diff)
    datafinetargetless_durata = cal42.getTime()
    logger.debug("sposto di x giorni (la differenza: %s) la datafinetarget a sinistra (sottraggo)" % data_diff)
    #Commento 17/03
    #wo.setValue("TARGCOMPDATE", datafinetarget, 11L)
    cal43 = Calendar.getInstance()
    cal43.setTime(datafinetarget)
    cal43.add(Calendar.MINUTE, +parent_DURATA)
    datafinetarget = cal43.getTime()
    logger.debug("Imposto datafinetarget %s al campo TARGCOMPDATE" % datafinetarget)
    #service.error("TEST",str(datafinetargetless_durata))
##########
#  12.1  #
##########
# old:
# inizi_solo_prop_min = min(inizi_solo_prop) if inizi_solo_prop else datafinetarget

# new (24/01):
dove = 0
inizio_min = None
if inizi_solo_prop:
    inizio_min = min(inizi_solo_prop)
    dove = 1
elif inizi_tutti_figli:
    inizio_min = min(inizi_tutti_figli)
    dove = 2
else:
    inizio_min = targstartdate_min

if inizio_min<inizio_max:
    inizio_min=datafinetargetless_durata
    dove = 3
# service.error("TEST","%s %s" % (inizio_min, dove) )

if inizi_solo_prop:
    logger.debug("Ottenuto il valore minimo dalla lista inizi_solo_prop: %s" % inizio_min)
# elif inizi_senza_prop:
#     logger.debug("Ottenuto il valore minimo dalla lista inizi_senza_prop: %s" % inizio_min)
logger.debug("Elaborazione ciclo `wo_children_senza_prop`:")


# subito dopo, gli altri che non hanno la propedeuticità, 
# li faccio "finire" (newfinish) dalla newstart dell'ordine che abbiamo preso prima;
# mi calcolo la newstart sempre come newfinish - durata

########
#  12  #
########
senza_prop_dates = dict()

def add_to_senza_prop_dates(mbo_child, nuova_targstart_date, nuova_targcomp_date):
    wonum = mbo_child.getString("WONUM")
    if wonum not in senza_prop_dates:
        senza_prop_dates[wonum] = dict()
    senza_prop_dates[wonum]["TARGSTARTDATE"] = nuova_targstart_date
    senza_prop_dates[wonum]["TARGCOMPDATE"] = nuova_targcomp_date
    

for line, mbo_child in enumerate(wo_children_senza_prop):
    durata = get_estdur(mbo_child)
    logger.debug("elaborazione mbo con WONUM: %s ..." % (mbo_child.getString("WONUM")))
    logger.debug("DURATA: %s" % durata)
    
    # calf = Calendar.getInstance()
    # prendo data min da inizi_solo_prop se inizi_solo_prop non e' vuoto altrimenti uso datafinetarget
    # calf.setTime(inizi_solo_prop_min)
    # calf.add(Calendar.HOUR, -durata)
    
    # prima:
    # nuova_targcomp_date = inizi_solo_prop_min # calf.getTime()
    
    # dopo (24/01/25):
    #nuova_targcomp_date = inizio_min
    nuova_targstart_date = inizio_min
    
    #_nuova_targstart_date = Calendar.getInstance()
    #_nuova_targstart_date.setTime(nuova_targcomp_date)
    #_nuova_targstart_date.add(Calendar.HOUR, -durata)
    #nuova_targstart_date = _nuova_targstart_date.getTime()
    
    _nuova_targcomp_date = Calendar.getInstance()
    _nuova_targcomp_date.setTime(nuova_targstart_date)
    _nuova_targcomp_date.add(Calendar.MINUTE, +durata)
    nuova_targcomp_date = _nuova_targcomp_date.getTime()
    
    #service.error("TEST","wonum "+str(mbo_child.getString("WONUM"))+", inizio target: "+str(nuova_targstart_date)+" e fine target: "+str(nuova_targcomp_date))
    debug = "targcompdate_min: %s \ndatafinetarget: %s\ndatafinetargetless_durata: %s\nfine_min: %s\ninizio_max: %s\nnuova_targcomp_date: %s" % (
        targcompdate_min,
        datafinetarget, 
        datafinetargetless_durata, 
        fine_min,
        inizio_max, 
        nuova_targcomp_date)
    
    
    logger.debug(debug)
    
    if fine_min and inizio_max:
        ########
        #  13  #
        ########
        # service.error("TEST", "inizio:"+str(inizio_max)+", targcomp:"+str(nuova_targcomp_date)+", fine:"+str(fine_min) + 
        #         "\ninizi tutti figli:%s" % inizi_tutti_figli + "\ninizi solo prop:%s" % inizi_solo_prop)
        
        if inizio_max <= nuova_targcomp_date < fine_min:
            ########
            #  16  #
            ########
            
            # if line == 0:
            #     service.error("debug", "wonum:" + mbo_child.getString("WONUM") + str(datainiziotarget_diff) + str(datafinetarget))
                
            # wonum: 28233  -- # Thu Oct 03 08:00:00 CEST 2024 -- # Sat Oct 05 00:00:00 CEST 2024
            # wonum: 29589  -- # Thu Oct 03 16:00:00 CEST 2024 -- # Sat Oct 05 00:00:00 CEST 2024
            # wonum: 29591  -- # Fri Oct 04 00:00:00 CEST 2024 -- # Sat Oct 05 00:00:00 CEST 2024
            mbo_child.setValue("TARGSTARTDATE", nuova_targstart_date, 11L)
            mbo_child.setValue("TARGCOMPDATE",  nuova_targcomp_date, 11L)
            
            add_to_senza_prop_dates(mbo_child, nuova_targstart_date, nuova_targcomp_date)
            continue
        else:
            ########
            #  14  #
            ########
            service.error("x3terna", "ErrorMsg", ["2 Calcolo impossibile"])
    elif fine_min and not inizio_max:
        if nuova_targcomp_date < fine_min:
            mbo_child.setValue("TARGSTARTDATE", nuova_targstart_date, 11L)
            mbo_child.setValue("TARGCOMPDATE", nuova_targcomp_date, 11L)
            
            add_to_senza_prop_dates(mbo_child, nuova_targstart_date, nuova_targcomp_date)
            continue
        else:
            service.error("x3terna", "ErrorMsg", ["3 Calcolo impossibile"])
    else:
        mbo_child.setValue("TARGSTARTDATE", nuova_targstart_date, 11L)
        mbo_child.setValue("TARGCOMPDATE", nuova_targcomp_date, 11L) 

    add_to_senza_prop_dates(mbo_child, nuova_targstart_date, nuova_targcomp_date)
        
#########
#  9.1  #
#########
# service.error("TEST non scatta con il primo click", "Inizio max: "+str(inizio_max)+", data fine:"+str(datafinetarget))
if datafinetarget > inizio_max:
    logger.debug("Entrato in datafinetargetless_durata > inizio_max")
    logger.debug("Elaborazione ciclo `wo_children_solo_prop`:")
    ########
    #  11  #
    ########
    
    # service.error("TEST", str([wo.getString("WONUM") for wo in wo_children_solo_prop ]))
    
    sssorted = list(sorted(wo_children_solo_prop, key=lambda x: x.getString("WONUM")))
    
    for mbo_prop in sssorted:
        logger.debug("elaborazione mbo con WONUM: %s ..." % mbo_prop)
        wonum = mbo_prop.getInt("WONUM")
        prop_WOEQ11 = mbo_prop.getString("WOEQ11")
        
        propedeutico_durata = get_estdur(mbo_prop)
        '''
        cal5 = Calendar.getInstance()
        cal5.setTime(datafinetarget)
        cal5.add(Calendar.MINUTE, -propedeutico_durata)
        
        datafinetarget_minus_self_durata_time = cal5.getTime()
        datafinetarget_minus_self_durata = datafinetarget_minus_self_durata_time
        
        
        inizi_solo_prop.append(datafinetarget_minus_self_durata)
        logger.debug("propedeutico_durata: %s\ndatafinetarget_minus_self_durata_time: %s" % (propedeutico_durata, datafinetarget_minus_self_durata_time))
        '''
        data_inizio_set = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
        data_inizio_set.setWhere("WONUM='{}'".format(mbo_prop.getString("WOEQ11")))
        data_inizio_set.reset()
        data_inizio = data_inizio_set.getMbo(0).getDate("TARGCOMPDATE")
        
        
        # service.error("riga 434 data_inizio", str(mbo_prop.getInt("WONUM")) + str(mbo_prop.getString("WOEQ11")) + "  "+  str(senza_prop_dates.items()))
        
        # 11.2
        #mbo_prop.setValue("TARGSTARTDATE", datafinetarget_minus_self_durata_time, 11L)       # Fri Oct 04 16:00:00 CEST 2024
        if prop_WOEQ11 in senza_prop_dates:
            # prendi TARGCOMPDATE dal predecessore
            data_inizio = senza_prop_dates[prop_WOEQ11]["TARGCOMPDATE"]
            mbo_prop.setValue("TARGSTARTDATE", data_inizio,11L)
        else:
            mbo_prop.setValue("TARGSTARTDATE", data_inizio,11L)
        
        cal5 = Calendar.getInstance()
        cal5.setTime(data_inizio)
        cal5.add(Calendar.MINUTE, propedeutico_durata)
        
        datafinetarget = cal5.getTime()
        # 11.1
        mbo_prop.setValue("TARGCOMPDATE", datafinetarget, 11L) # Fri Oct 04 16:00:00 CEST 2024
        
        if prop_WOEQ11 in senza_prop_dates:
            add_to_senza_prop_dates(mbo_prop, data_inizio, datafinetarget)
        
        # service.error("11.1", mbo_prop.getString("WONUM") + " "+str(datafinetarget_minus_self_durata_time))
else:
    # NB: se non è così, allora devo spostare tutto a destra con il ragionamento fatto sotto 
    # (differenza tra TARGCOMPDATE-DURATA-INIZIOMAX, se questo è negativo, 
    # devo aumentare la TARGCOMPDATE dei giorni in assoluto di questa differenza
    
    ########
    #  10  #
    ########
    
    # service.error("qwe", "aaa")
    
    # se wo_children_solo_prop e' vuoto...
    for mbo in wo_children_senza_prop:
        logger.debug("elaborazione mbo senza prop con WONUM: %s ..." % mbo)
        targcompdate = mbo.getDate("TARGCOMPDATE")
        durata = get_estdur(mbo)
        cal5 = Calendar.getInstance()
        cal5.setTime(targcompdate)
        cal5.add(Calendar.MINUTE, -durata)
        
        # service.error("a", str(mbo.getInt("WONUM")))

        targcompdate_less_inizio_max = Calendar.getInstance()
        targcompdate_less_inizio_max.setTime(targcompdate)
        targcompdate_less_inizio_max.add(Calendar.MINUTE, -find_date_diff(targcompdate, inizio_max))
        targcompdate_less_inizio_max = targcompdate_less_inizio_max.getTime()
        
        data_diff = find_date_diff(cal5.getTime(),inizio_max)
        
        # service.error("bbb", "targcompdate: %s \n targcompdate_less_inizio_max: %s \n data_diff: %s \n getTime: %s \n inizio_max: %s" % (targcompdate, 
        #     targcompdate_less_inizio_max, data_diff, cal5.getTime(), inizio_max))
        
        cal5.add(Calendar.MINUTE, -data_diff)
        targcompdate_less_self_durata_and_inizio_max_diff = cal5.getTime()
        
        mbo.setValue("TARGSTARTDATE", targcompdate_less_self_durata_and_inizio_max_diff, 11L)   
        mbo.setValue("TARGCOMPDATE", targcompdate_less_inizio_max, 11L) 

        inizi_tutti_figli.append(targcompdate_less_self_durata_and_inizio_max_diff)
        add_to_senza_prop_dates(mbo, targcompdate_less_self_durata_and_inizio_max_diff, targcompdate_less_inizio_max)
        update_prop.append(mbo.getString("WONUM"),targcompdate_less_inizio_max)
        logger.debug("durata: %s\ndatafinetarget_minus_self_durata_time: %s" % (durata, targcompdate_less_self_durata_and_inizio_max_diff))

    for mbo in wo_children_solo_prop:
        sssorted = list(sorted(wo_children_solo_prop, key=lambda x: x.getString("WONUM")))
    
        for mbo_prop in sssorted:
            logger.debug("elaborazione mbo con WONUM: %s ..." % mbo_prop)
            wonum = mbo_prop.getInt("WONUM")
            prop_WOEQ11 = mbo_prop.getInt("WOEQ11")
            
            propedeutico_durata = get_estdur(mbo_prop)
            '''
            cal5 = Calendar.getInstance()
            cal5.setTime(datafinetarget)
            cal5.add(Calendar.MINUTE, -propedeutico_durata)
            
            datafinetarget_minus_self_durata_time = cal5.getTime()
            datafinetarget_minus_self_durata = datafinetarget_minus_self_durata_time
            
            
            inizi_solo_prop.append(datafinetarget_minus_self_durata)
            logger.debug("propedeutico_durata: %s\ndatafinetarget_minus_self_durata_time: %s" % (propedeutico_durata, datafinetarget_minus_self_durata_time))
            '''
            for predecessore in update_prop:
                if update_prop[predecessore][0]==mbo_prop.getString("WOEQ11"):
                    data_inizio=update_prop[predecessore][1]
                else:
                    continue
            '''data_inizio_set = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
            data_inizio_set.setWhere("WONUM='{}'".format(mbo_prop.getString("WOEQ11")))
            data_inizio_set.reset()'''
            
            data_inizio = data_inizio_set.getMbo(0).getDate("TARGCOMPDATE")
            # service.error("riga 434 data_inizio", str(mbo_prop.getInt("WONUM")) + str(mbo_prop.getString("WOEQ11")) + "  "+  str(senza_prop_dates.items()))
            
            # 11.2
            #mbo_prop.setValue("TARGSTARTDATE", datafinetarget_minus_self_durata_time, 11L)       # Fri Oct 04 16:00:00 CEST 2024
            if prop_WOEQ11 in senza_prop_dates:
                # prendi TARGCOMPDATE dal predecessore
                data_inizio = senza_prop_dates[prop_WOEQ11]["TARGCOMPDATE"]
                mbo_prop.setValue("TARGSTARTDATE", data_inizio,11L)
            else:
                mbo_prop.setValue("TARGSTARTDATE", data_inizio,11L)
            
            cal5 = Calendar.getInstance()
            cal5.setTime(data_inizio)
            cal5.add(Calendar.MINUTE, propedeutico_durata)
            
            datafinetarget = cal5.getTime()
            # 11.1
            mbo_prop.setValue("TARGCOMPDATE", datafinetarget, 11L) # Fri Oct 04 16:00:00 CEST 2024
            
            if prop_WOEQ11 in senza_prop_dates:
                add_to_senza_prop_dates(mbo_prop, data_inizio, datafinetarget)
                
wo_children.save()
#wo_children.reset()
#wo_children.close()
#wo_children.cleanup()

#
########
#  15  #
########
'''
service.error("1", """fine_min: %s\n
                inizio_min: %s\n
                parent_TARGSTARTDATE: %s\n
                parent_TARGCOMPDATE: %s\n
                inizio_max: %s""" % (fine_min , inizio_min,parent_TARGSTARTDATE, parent_TARGCOMPDATE, inizio_max))
'''

if fine_min and inizio_max:
    # qui forse cé' un errore: perche' datafinetarget che deve andare in TARGCOMPDATE sarebbe la data minore dei figli, ed e' fissa
    # mentre la data per il campo datainiziotarget_diff e' stata calcolata dinamicamente in ogni iterazione=mbo figlio,
    # che a sua volta deriva dai punti 12.1, e ancora prima punto, 6, 7, 2.
    wo.setValue("WOJP2","INPRG")
    wo.getThisMboSet().save()  # Salvataggio
    wo.getThisMboSet().reset()  # Resetta e ricarica il set di MBO
    wo = wo.getThisMboSet().getMboForUniqueId(mboKey)
    wo_children_set=wo.getMboSet("CHILDNOTASK")
    min_targ_start = None
    max_targ_comp = None
    min_sne_constraint = None
    max_fnl_constraint = None
    targ_start=None
    targ_comp=None
    sne_constraint=None
    fnl_constraint=None
    for figlio in range(0,wo_children_set.count()):
        childMbo=wo_children_set.getMbo(figlio)
        if childMbo:
            targ_start = childMbo.getDate("TARGSTARTDATE")
            targ_comp = childMbo.getDate("TARGCOMPDATE")
            sne_constraint = childMbo.getDate("SNECONSTRAINT")
            fnl_constraint = childMbo.getDate("FNLCONSTRAINT")

        if (min_targ_start is None or targ_start < min_targ_start):
            min_targ_start = targ_start
        
        if (max_targ_comp is None or targ_comp > max_targ_comp):
            max_targ_comp = targ_comp
        
        if (min_sne_constraint is None or sne_constraint > min_sne_constraint):
            min_sne_constraint = sne_constraint
        
        if (max_fnl_constraint is None or fnl_constraint < max_fnl_constraint):
            max_fnl_constraint = fnl_constraint
            
    
    # service.error("TEST","fine_min: %s, inizio_max: %s, max_targ_comp: %s" % (fine_min, inizio_max, max_targ_comp))
            

    #service.error("TEST",str(nuova_targcomp_date))
    #wo.setValue("TARGCOMPDATE", datafinetarget) # Sat Oct 05 00:00:00 CEST 2024
    #wo.setValue("TARGCOMPDATE", nuova_targcomp_date) # Sat Oct 05 00:00:00 CEST 2024
    #_padre_new_targstartdate = Calendar.getInstance()
    #_padre_new_targstartdate.setTime(nuova_targcomp_date)
    #_padre_new_targstartdate.setTime(datafinetarget)
    #_padre_new_targstartdate.add(Calendar.HOUR, -parent_DURATA)
    #padre_new_targstartdate = _padre_new_targstartdate.getTime()
    #wo.setValue("TARGSTARTDATE", padre_new_targstartdate, 11L)
    wo.setValue("TARGSTARTDATE", min_targ_start, 11L)
    wo.setValue("TARGCOMPDATE", max_targ_comp, 11L)
    #service.error("TEST",str(parent_DURATA/60))
    #wo.setValue("ESTDUR", parent_DURATA/60)
    #service.error("TEST",str(min_targ_start))
    #service.error("TEST",str(min_sne_constraint))
    wo.setValue("SNECONSTRAINT", min_sne_constraint, 11L)
    wo.setValue("FNLCONSTRAINT", max_fnl_constraint, 11L)
    wo.setValueNull("WOJP2")
    if not wo.isNull("X4STATUSINDISP"):
        PublishChannelCache.getInstance().getPublishChannel("X4ODLAGILE2").publish(wo, True);
    wo.getThisMboSet().save()
    
    



#wo.getThisMboSet().save()
# wo_children = mbo.getMboSet("CHILDNOTASK")
# for i in range(0,wo_children.count()):
#     wo_childrenmbo=wo_children.getMbo(i)
#     wo_childrenmbo.setValue("TARGSTARTDATE","10.11.2024 7:20 PM",11L)
#     wo_childrenmbo.setValue("TARGCOMPDATE","11.11.2024 7:20 PM",11L)
# mbo.setValue("TARGSTARTDATE","10.11.2024 7:20 PM",11L)
# mbo.setValue("TARGCOMPDATE","11.11.2024 7:20 PM",11L)
# mbo.setValue("SNECONSTRAINT","01.11.2024 7:20 PM",11L)
# mbo.setValue("FNLCONSTRAINT","11.11.2029 7:20 PM",11L)