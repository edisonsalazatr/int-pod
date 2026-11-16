from psdi.server import MXServer
from psdi.mbo import MboConstants
from psdi.util.logging import MXLoggerFactory
from java.text import SimpleDateFormat 
from java.util import Calendar

mxServer = MXServer.getMXServer()
userInfo = mxServer.getSystemUserInfo()
logger = MXLoggerFactory.getLogger("maximo.script.X3RIEPILOGO")

field_QTYACC = "QTYACC"
field_HPROPRIETEO = "HPROPRIETEO"
field_QTYINV = "QTYINV"
field_STATUS = "STATUS"
field_ID = "X4CAPACITYID"  
field_CAPACITYHTEO_PM = "CAPACITYHTEO"
field_ORE_DET = "ORE"
field_CEDUTOTEO = "CEDUTOTEO"
field_RICEVUTOTEO = "RICEVUTOTEO"

not_processed = list()
HPROPRIEPLAN_values_test = list()
wplabor_months = dict()
wplabor_months_hours = dict()
x4capacity_related_to_mese_craft = dict()
x4capacity_month_craft_flat = dict()

months = ["GENNAIO", "FEBBRAIO", "MARZO", "APRILE", "MAGGIO", "GIUGNO",
        "LUGLIO", "AGOSTO", "SETTEMBRE", "OTTOBRE", "NOVEMBRE", "DICEMBRE"]
month_id_to_name = dict(enumerate(months, 1))


def parse_date(date_str):
    sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    date = sdf.parse(date_str)
    cal = Calendar.getInstance()
    cal.setTime(date)
    return cal


def distribuisci_ore(inizio, fine, wplaborhours, mese, craft, months_hours):
    current_date = inizio.clone()
    months = get_months_between(inizio, fine)
    total_minutes = (fine.getTimeInMillis() - inizio.getTimeInMillis()) / (60 * 1000)  # Totale minuti nel range "inizio -> fine"
    if len(months) == 1:
        if total_minutes == 0:
            calc = 1 * wplaborhours
        else:
            calc = (total_minutes / total_minutes) * wplaborhours
        month_id = inizio.get(Calendar.MONTH) + 1
        month_name = month_id_to_name[month_id]
        if month_name not in months_hours:
            months_hours[month_name] = dict()
        if craft not in months_hours[month_name]:
            months_hours[month_name][craft] = 0.0
        months_hours[month_name][craft] += calc
        return months_hours
    else:
        if total_minutes == 0:
            return months_hours
        while current_date.before(fine):
            next_day = current_date.clone()
            next_day.add(Calendar.DAY_OF_MONTH, 1)
            next_day.set(Calendar.HOUR_OF_DAY, 0)
            next_day.set(Calendar.MINUTE, 0)
            next_day.set(Calendar.SECOND, 0)
            if next_day.after(fine):
                next_day = fine.clone()
            daily_minutes = (next_day.getTimeInMillis() - current_date.getTimeInMillis())  / (1000 * 60)  # minutes
            month_id = current_date.get(Calendar.MONTH) + 1
            month_name = month_id_to_name[month_id]
            if mese != month_name:
                if month_name not in months_hours:
                    months_hours[mese] = {craft: 0.0}
            if month_name not in months_hours:
                months_hours[month_name] = dict()
            if craft not in months_hours[month_name]:
                months_hours[month_name][craft] = 0.0
            months_hours[month_name][craft] += (float(daily_minutes) / float(total_minutes)) * wplaborhours
            current_date = next_day.clone()
    return months_hours
    

def get_months_between(inizio_cal, fine_cal):
    cal = (inizio_cal.clone())
    cal.set(Calendar.DAY_OF_MONTH, 1)
    months = list()
    while cal.before(fine_cal) or cal.equals(fine_cal):
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH) + 1
        last_day = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        months.append((year, month, last_day))
        cal.add(Calendar.MONTH, 1)
    return months


related_pm_mbo_dup_set = mbo.getMboSet("X4CAPACITYPM")
related_pm_mbo_list = [related_pm_mbo_dup_set.getMbo(i) for i in range(0, related_pm_mbo_dup_set.count())]
related_pm_mbo_month_craft_mapped = {"%s_%s" % (capacity.getString("MESE"), capacity.getString("CRAFT")): capacity \
                                    for capacity in related_pm_mbo_list}

related_pm_mbo_dup = related_pm_mbo_dup_set.moveFirst()

# #################################################################
# Cicla x4capacitypm e i suoi wplabor per ottenere i valori 
# delle date e laborhours da impostare a x4capacitypm.HPROPRIEPLAN
# #################################################################

# ###
# DEBUG <<< code placeholder  >>>
# ###

while related_pm_mbo_dup:
    months_hours = dict()
    x4capacitypmid = related_pm_mbo_dup.getDouble("X4CAPACITYPMID")
    pmSet = mxServer.getMboSet("X4CAPACITYPM", mbo.getUserInfo())
    pmSet.setWhere("X4CAPACITYPMID=%s" % x4capacitypmid)
    capacityPm = pmSet.getMbo(0)
   
    # x4capacitypm related variables
    X4CAPACITYPMID = capacityPm.getInt("X4CAPACITYPMID")
    CRAFT = capacityPm.getString("CRAFT")
    MESE = capacityPm.getString("MESE")
    wolaborset = capacityPm.getMboSet("X4WPLABOR")
    laborMbo = wolaborset.moveFirst()
    
    """
    wonum_to_status = dict()
    wonum_list = [str(wolaborset.getMbo(i).getInt("WONUM")) for i in range(0, wolaborset.count())]
    if wonum_list:
        wonums = ", ".join(wonum_list)
        wo_statuses_set = mxServer.getMboSet("WORKORDER",mbo.getUserInfo())
        wo_statuses_set.setWhere("WONUM in (%s) AND STATUS NOT IN ('DA VALIDARE', NON VALIDATO', 'NON ESEGUITO')" % wonums)
        if not wo_statuses_set.isEMpty():
            for i in range(0, wo_statuses_set.count()):
                odl = wo_statuses_set.getMbo(i)
                wonum_to_status[odl.getInt("WONUM")] = odl.getString("STATUS")
    # service.error("")
    """
    
    while laborMbo:
        # wplabor record related variables
        laborid = laborMbo.getString("WPLABORID")
        total_LABORHRS = (laborMbo.getDouble("LABORHRS") + laborMbo.getDouble("X4ORETRASF")) * laborMbo.getDouble("QUANTITY")
        x4datainiziorisorsa = laborMbo.getDate("X4DATAINIZIORISORSA")
        x4datafinerisorsa = laborMbo.getDate("X4DATAFINERISORSA")
        if not laborMbo or not x4datainiziorisorsa or not x4datafinerisorsa:
            laborMbo = wolaborset.moveNext()
            continue
        cal_inizio = Calendar.getInstance()
        cal_inizio.setTime(x4datainiziorisorsa)
        cal_fine = Calendar.getInstance()
        cal_fine.setTime(x4datafinerisorsa)
        mese_inizio = cal_inizio.get(Calendar.MONTH) + 1
        mese_fine = cal_fine.get(Calendar.MONTH) + 1
        date_obj_inizio = cal_inizio.getTime()
        date_obj_fine = cal_fine.getTime()
        
        # Calcolo distribuzione ore
        distribuzione = distribuisci_ore(cal_inizio, cal_fine, total_LABORHRS, MESE, CRAFT, months_hours)
        if distribuzione:
            if X4CAPACITYPMID not in wplabor_months:
                wplabor_months[X4CAPACITYPMID] = None
            wplabor_months[X4CAPACITYPMID] = distribuzione
            key = "%s_%s" % (MESE, CRAFT)

            # Struttura di appoggio: memorizzare per quali key (mese+craft) sono stati calcolati gli orari
            # Ogni Mese+Craft puo'ricevere piu' valori da diversi wplabor, che poi verranno sommati
            if key not in x4capacity_month_craft_flat:
                x4capacity_month_craft_flat[key] = list()
            if X4CAPACITYPMID not in x4capacity_month_craft_flat[key]:
                x4capacity_month_craft_flat[key].append(X4CAPACITYPMID)

        laborMbo = wolaborset.moveNext()  # fine iterazione per wplabor
    wolaborset.close()
    pmSet.close()
    related_pm_mbo_dup = related_pm_mbo_dup_set.moveNext()  # fine iterazione per x4capacitypm
related_pm_mbo_dup_set.close()

# Struttura di appogio
# Spartisce x4capacitypm in due strutture:
# 1) Quella da aggiornare subito nella sua iterazione
# 2) Quella che si costruisce dalle date cross-mese, 
# ... che pero' a sua volta possono avere un suo x4capacitypm con lo stesso mese e craft
for X4CAPACITYPMID, data in wplabor_months.items():
    for mese, crafts in data.items():
        craft = list(crafts.keys())[0]
        hours = crafts[craft]
        key = "%s_%s" % (mese, craft)
        if key in x4capacity_month_craft_flat:
            key_capacity_id = x4capacity_month_craft_flat[key][0]
            if key_capacity_id not in wplabor_months_hours:
                wplabor_months_hours[key_capacity_id] = 0
            wplabor_months_hours[key_capacity_id] += hours
        else:
            capacity_to_query = key
            if capacity_to_query not in wplabor_months_hours:
                wplabor_months_hours[capacity_to_query] = 0
            wplabor_months_hours[capacity_to_query] += hours
            if X4CAPACITYPMID not in x4capacity_related_to_mese_craft:
                x4capacity_related_to_mese_craft[X4CAPACITYPMID] = list()
            x4capacity_related_to_mese_craft[X4CAPACITYPMID].append(capacity_to_query)
            


related_pm_mbo_set = mbo.getMboSet("X4CAPACITYPM")
related_pm_mbo_list = [related_pm_mbo_set.getMbo(i) for i in range(0, related_pm_mbo_set.count())]
related_pm_mbo_ids = [_.getInt("X4CAPACITYPMID") for _ in related_pm_mbo_list]
related_pm_mbo_month_craft_mapped = {"%s_%s" % (capacity.getString("MESE"), capacity.getString("CRAFT")): capacity \
                                    for capacity in related_pm_mbo_list}
                                    
# Struttura (rif.01) che contiene solo gli X4CAPACITYPM del secondo mese dei periodi cross-mese, 
# esempio: {MESE_CRAFT: mbo}
related_pm_mbo_month_craft_mapped_filtered_by_key = {
    k: v for k,v in related_pm_mbo_month_craft_mapped.items() \ 
    if k in [i[0] for i in x4capacity_related_to_mese_craft.values()]
}
x4capacitypm_hproprieplans = {x4c.getInt("X4CAPACITYPMID"): x4c.getFloat("HPROPRIEPLAN") \
    for x4c in related_pm_mbo_list if x4c.getInt("X4CAPACITYPMID") in wplabor_months or \
    x4c.getInt("X4CAPACITYPMID") in related_pm_mbo_month_craft_mapped_filtered_by_key}
# for x4c in related_pm_mbo_month_craft_mapped_filtered_by_key.values():
#     x4capacitypm_hproprieplans[x4c.getInt("X4CAPACITYPMID")] = x4c.getFloat("HPROPRIEPLAN")

# service.error("wplabor_months",  str(wplabor_months.items()))

# Struttura: X4CAPACITYPMID mappato a ore
month_craft_keys_mapped_to_x4capacitypmids = dict()
for capacity in related_pm_mbo_list:
    key = "%s_%s" % (capacity.getString("MESE"), capacity.getString("CRAFT"))
    if key not in related_pm_mbo_month_craft_mapped_filtered_by_key:
        #capacity.setValue("HPROPRIEPLAN",0)
        continue
    X4CAPACITYPMID = capacity.getInt("X4CAPACITYPMID")
    if X4CAPACITYPMID not in month_craft_keys_mapped_to_x4capacitypmids:
        month_craft_keys_mapped_to_x4capacitypmids[X4CAPACITYPMID] = list()
    hours = wplabor_months_hours[key]
    month_craft_keys_mapped_to_x4capacitypmids[X4CAPACITYPMID].append(hours)


# Imposta HPROPLAN hours prima alle x4capacitypm cross-mese, !!! a quelle del secondo mese (quelli della struttura)
# x4capacitypm_cross_month_set = None
#service.error("TEST",str(month_craft_keys_mapped_to_x4capacitypmids.keys()))
if month_craft_keys_mapped_to_x4capacitypmids.keys():
    x4capacitypm_cross_month_set = MXServer.getMXServer().getMboSet("X4CAPACITYPM", mbo.getUserInfo())
    x4capacitypm_cross_month_set.setWhere("X4CAPACITYPMID in (%s)" % ", ".join(map(str, month_craft_keys_mapped_to_x4capacitypmids.keys())))

    if not x4capacitypm_cross_month_set.isEmpty():
        for i in range(0, x4capacitypm_cross_month_set.count()):
            x4capacity_mbo = x4capacitypm_cross_month_set.getMbo(i)
            X4CAPACITYPMID = x4capacity_mbo.getInt("X4CAPACITYPMID")
            #HPROPRIEPLAN = x4capacity_mbo.getFloat("HPROPRIEPLAN")
            hours = round(sum(month_craft_keys_mapped_to_x4capacitypmids[X4CAPACITYPMID]), 2)
            x4capacity_mbo.setValue("HPROPRIEPLAN", hours, MboConstants.NOVALIDATION_AND_NOACTION | MboConstants.NOACCESSCHECK)
            x4capacitypm_cross_month_set.save()
        x4capacitypm_cross_month_set.close()


# if X4CAPACITYPMID in month_craft_keys_mapped_to_x4capacitypmids:
#     diff_month_craft_mbo = related_pm_mbo_month_craft_mapped_filtered_by_key[mese_craft_key]
#     X4CAPACITYPMID_CROSSMESE = diff_month_craft_mbo.getInt("X4CAPACITYPMID")
#     # service.error("hours", str(hours))
#     diff_month_craft_mbo.setValue("HPROPRIEPLAN", hours, MboConstants.NOVALIDATION_AND_NOACTION | MboConstants.NOACCESSCHECK)

related_pm_mbo = related_pm_mbo_set.moveFirst()
while related_pm_mbo:
    x4capacitypmid = related_pm_mbo.getDouble("X4CAPACITYPMID")
    pmSet = mxServer.getMboSet("X4CAPACITYPM",mbo.getUserInfo())
    pmSet.setWhere("X4CAPACITYPMID=%s" % x4capacitypmid)
    capacityPm = pmSet.getMbo(0)
    HPROPRIEPLAN_initial = capacityPm.getFloat("HPROPRIEPLAN")
    
    logger.info(" - X4CAPACITY calcolo PM - numero: "+str(x4capacitypmid) )
    logger.info(" - X4CAPACITY calcolo PM - mese: "+str(capacityPm.getString("MESE")) )
    logger.info(" - X4CAPACITY calcolo PM - capacityid: "+str(capacityPm.getString("X4CAPACITYID")) )
    related_details_mbo_set = capacityPm.getMboSet("X4CAPACITYDET")
    related_details_mbo = related_details_mbo_set.moveFirst()
    related_exc_capacity_set = capacityPm.getMboSet("X4CAPACITYEXCHANGE_F")
    if not related_exc_capacity_set.isEmpty():
        related_exc_mbo = related_exc_capacity_set.moveFirst()
        ore_value = related_details_mbo.getDouble(field_ORE_DET)
    else:
        ore_value= 0
    if related_details_mbo is not None:
        # Pick ORE value from X4CAPACITYDETAILS
        ore_value = related_details_mbo.getDouble(field_ORE_DET)
        if capacityPm is not None:
            # Update CAPACITYHTEO in X4CAPACITYPM
            capacityPm.setValue(field_CAPACITYHTEO_PM, round(ore_value,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
            #related_pm_mbo_set.save()

    # Calculate the total QTYACC
    total_qtyacc = 0
    if not related_exc_capacity_set.isEmpty():
        while related_exc_mbo:
            total_qtyacc_part = related_exc_capacity_set.getDouble(field_QTYACC)
            total_qtyacc += total_qtyacc_part
            related_exc_mbo = related_exc_capacity_set.moveNext()

    HPROPRIETEO = ore_value-total_qtyacc
    if capacityPm is not None:
        capacityPm.setValue(field_CEDUTOTEO, round(total_qtyacc,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
        capacityPm.setValue(field_HPROPRIETEO, round(HPROPRIETEO,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
        #related_pm_mbo_set.save()

    '''
    woMboSet = capacityPm.getMboSet("X4WORKORDER")
    woMboSet.reset()
    '''
    # Calculate total_LABORHRS for all related WO
    X4CAPACITYPMID = capacityPm.getInt("X4CAPACITYPMID")
    MESE = capacityPm.getString("MESE")
    CRAFT = capacityPm.getString("CRAFT")
    mese_craft_key = "%s_%s" % (MESE, CRAFT)
    total_LABORHRS = 0
    
    # Ottieni valori delle ore calcolate per mese e craft
    # ... nella stessa iterazione potrebbe impostare ore a due x4capacitypm differenti
    if X4CAPACITYPMID in wplabor_months_hours:
        hours = wplabor_months_hours[X4CAPACITYPMID]
    else:
        hours = 0
    # initial_hproprieplan = x4capacitypm_hproprieplans.pop(X4CAPACITYPMID, 0)
    # hours = initial_hproprieplan + hours
    capacityPm.setValue("HPROPRIEPLAN", round(hours, 2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)

    # if X4CAPACITYPMID in month_craft_keys_mapped_to_x4capacitypmids:
    #     diff_month_craft_mbo = related_pm_mbo_month_craft_mapped_filtered_by_key[mese_craft_key]
    #     X4CAPACITYPMID_CROSSMESE = diff_month_craft_mbo.getInt("X4CAPACITYPMID")
    #     hours = round(sum(month_craft_keys_mapped_to_x4capacitypmids[X4CAPACITYPMID]), 2)
    #     hours = hours + x4capacitypm_hproprieplans.pop(X4CAPACITYPMID_CROSSMESE, 0)
    #     # service.error("hours", str(hours))
    #     diff_month_craft_mbo.setValue("HPROPRIEPLAN", hours, MboConstants.NOVALIDATION_AND_NOACTION | MboConstants.NOACCESSCHECK)

    # Salva valori
    related_pm_mbo_set.save(MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    
    # else:
    #     # pass
    #     wolaborset = capacityPm.getMboSet("X4WPLABOR")  # era aperto in originale
    #     laborMbo = wolaborset.moveFirst()
    #     while laborMbo: 
    #         total_LABORHRS = (laborMbo.getDouble("LABORHRS") + laborMbo.getDouble("X4ORETRASF")) * laborMbo.getDouble("QUANTITY")
    #         X4DATAINIZIORISORSA = laborMbo.getDate("X4DATAINIZIORISORSA")
    #         X4DATAFINERISORSA = laborMbo.getDate("X4DATAFINERISORSA")
    #         if not X4DATAINIZIORISORSA or not X4DATAFINERISORSA:
    #             laborMbo = wolaborset.moveNext()
    #             continue
    #         HPROPRIEPLAN_values_test.append((X4CAPACITYPMID, laborMbo.getString("WPLABORID"), MESE, CRAFT, 
    #             X4DATAINIZIORISORSA,
    #             X4DATAFINERISORSA, 
    #             total_LABORHRS))
    #         laborMbo = wolaborset.moveNext()
    #     wolaborset.close()
    # pmSet.close()

    '''
    # Calculate total_REGULARHRS for all related WO
    woMboSet2 = mbo.getMboSet("X4WORKORDER")
    total_REGULARHRS = 0
    if woMboSet2:
        woMbo2 = woMboSet2.moveFirst()
        while woMbo2:
            # Pick de LABTRANS 
            woactlaborset = woMbo2.getMboSet("LABTRANS")
            if woactlaborset:
                actlaborMbo = woactlaborset.moveFirst()
                while actlaborMbo:
                    total_REGULARHRS += actlaborMbo.getDouble("REGULARHRS")
                    actlaborMbo = woactlaborset.moveNext()
            woMbo2 = woMboSet2.moveNext()

        woMboSet2.close()
        
    cedutoteo_value = capacityPm.getDouble("CEDUTOTEO")
    if total_REGULARHRS > cedutoteo_value:
        errorgroup = "custom"
        errorkey = "CEDUTOPLANCannotExceedCEDUTOTEO"
    else:
        capacityPm.setValue("CEDUTOPLAN", total_REGULARHRS, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
        #related_pm_mbo_set.save(MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    '''
    
    #Calculate CEDUTOPLAN
    total_CEDUTOPLAN = 0
    wocedutoset = capacityPm.getMboSet("X4CEDUTOPLAN")
    woceduto = wocedutoset.moveFirst()
    while woceduto:
        total_CEDUTOPLAN += (woceduto.getDouble("LABORHRS")+ woceduto.getDouble("X4ORETRASF"))*woceduto.getDouble("QUANTITY")
        woceduto = wocedutoset.moveNext()

        #woMbo = woMboSet.moveNext()  
    logger.info(" - X4CAPACITY ceduto plan: "+str(total_CEDUTOPLAN) )
    
    # service.error("CEDUTOPLAN", str(total_CEDUTOPLAN))
    
    capacityPm.setValue("CEDUTOPLAN", round(total_CEDUTOPLAN,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)    
    wocedutoset.close()    
    # Calcular il valore di SATURAZIONEPLAN come (HPROPRIEPLAN + CEDUTOPLAN) / CAPACITYHTEO * 100

    hproprieplan_value = capacityPm.getDouble("HPROPRIEPLAN")
    cedutoplan_value = capacityPm.getDouble("CEDUTOPLAN")
    capacityhteo_value = capacityPm.getDouble("CAPACITYHTEO")

    if capacityhteo_value > 0:  # Evitar la división por cero
        saturazione_plan = (hproprieplan_value + cedutoplan_value)* 100 / capacityhteo_value 
        capacityPm.setValue("SATURAZIONEPLAN", round(saturazione_plan,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
        #related_pm_mbo_set.save(MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    else:
        # Case when CAPACITYHTEO es 0 or null
        capacityPm.setValue("SATURAZIONEPLAN", 0, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
        #related_pm_mbo_set.save(MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
        # Calcular il valore di CONTOBASEPLAN come CAPACITYHTEO - HPROPRIEPLAN - CEDUTOPLAN - SERBATOIO

    capacityhteo_value = capacityPm.getDouble("CAPACITYHTEO")
    hproprieplan_value = capacityPm.getDouble("HPROPRIEPLAN")
    cedutoplan_value = capacityPm.getDouble("CEDUTOPLAN")
    serbatoio_value = capacityPm.getDouble("SERBATOIO")

    # Calcular CONTOBASEPLAN
    contobaseplan = capacityhteo_value - hproprieplan_value - cedutoplan_value
    #if contobaseplan<0:
        #service.error("x3terna","ErrorMsg",["Il valore del conto base pianificato per il mese "+str(capacityPm.getString("MESE"))+" risulta negativo. Si prega di correggere il valore di serbatoio inserito."])

    #Update CONTOBASEPLAN 
    capacityPm.setValue("CONTOBASEPLAN", round(contobaseplan,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    #related_pm_mbo_set.save(MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)

    # Calcular il valore di DELTACEDUTO come CEDUTOTEO - CEDUTOPLAN

    cedutoteo_value = capacityPm.getDouble("CEDUTOTEO")
    cedutoplan_value = capacityPm.getDouble("CEDUTOPLAN")

    # Calcular DELTACEDUTO
    deltaceduto = cedutoteo_value - cedutoplan_value

    # Update DELTACEDUTO
    capacityPm.setValue("DELTACEDUTO", round(deltaceduto,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    #related_pm_mbo_set.save(MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)

    # Calcular il valore di RICEVUTOPLAN come somma delle ore assorbite negli OdL dell'UdB ricevute da altre UdB
    '''
    total_ricevutoplan = 0

    # Obtener las horas de trabajo absorbidas desde otras UdB
    
    woMboSet = mbo.getMboSet("X4WORKORDER2")
    if woMboSet:
        woMbo = woMboSet.moveFirst()
        while woMbo:
            # Obtener el MboSet relacionado de LABTRANS solo si hay registros
            woactlaborset = woMbo.getMboSet("WPLABOR")
            if woactlaborset:
                actlaborMbo = woactlaborset.moveFirst()
                while actlaborMbo:
                    # Sumar las horas absorbidas
                    total_ricevutoplan += actlaborMbo.getDouble("LABORHRS")
                    actlaborMbo = woactlaborset.moveNext()
            woMbo = woMboSet.moveNext()

        woMboSet.close()

    #Get  CEDUTOTEO from UdB cedente
    cedutoteo_cedente_value = capacityPm.getDouble("CEDUTOTEO")

    # Check if  RICEVUTOPLAN is bigger than CEDUTOTEO on UdB cedente
    if total_ricevutoplan > cedutoteo_cedente_value:
        # Error message if RICEVUTOPLAN is bigger than CEDUTOTEO
        errorgroup = "custom"
        errorkey = "ERRORKEY", "RICEVUTOPLANCannotExceedCEDUTOTEO"
    else:
        # Update RICEVUTOPLAN
        capacityPm.setValue("RICEVUTOPLAN", total_ricevutoplan, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
        #related_pm_mbo_set.save()
    '''
    total_RICEVUTOPLAN = 0
    woRicPianset = capacityPm.getMboSet("X4RICPIANIFICATO")
    woRicPian = woRicPianset.moveFirst()
    while woRicPian:
        total_RICEVUTOPLAN += (woRicPian.getDouble("LABORHRS")+woRicPian.getDouble("X4ORETRASF"))*woRicPian.getDouble("QUANTITY")
        woRicPian = woRicPianset.moveNext()

        #woMbo = woMboSet.moveNext()    
    logger.info(" - X4CAPACITY ricevuto plan: "+str(total_RICEVUTOPLAN) )
    capacityPm.setValue("RICEVUTOPLAN", round(total_RICEVUTOPLAN,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    #related_pm_mbo_set.save(MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    woRicPianset.close()
    
    #Calcolo di ricevuto Teorico
    ricevutoTeoSet= capacityPm.getMboSet("X4RICEVUTOTEO")
    ricevutoTeo = ricevutoTeoSet.moveFirst()
    total_qtyric = 0
    while ricevutoTeo:
        total_qtyric_part = ricevutoTeo.getDouble(field_QTYACC)
        total_qtyric += total_qtyric_part
        logger.info(" - X4CAPACITY calcolo ricevuto - numero: "+str(total_qtyric) )
        ricevutoTeo = ricevutoTeoSet.moveNext()
    capacityPm.setValue(field_RICEVUTOTEO, round(total_qtyric,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    ricevutoTeoSet.close()
    
    # Calcular il valore di DELTARICEVUTO come RICEVUTOTEO - RICEVUTOPLAN

    ricevutoteo_value = capacityPm.getDouble("RICEVUTOTEO")
    ricevutoplan_value = capacityPm.getDouble("RICEVUTOPLAN")

    # Calcular DELTARICEVUTO
    deltaricevuto = ricevutoteo_value - ricevutoplan_value

    # Update DELTARICEVUTO
    capacityPm.setValue("DELTARICEVUTO", round(deltaricevuto,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    #related_pm_mbo_set.save()

    # Calcular il valore di FLAVOROTEO come HPROPRIETEO + RICEVUTOTEO

    hproprieteo_value = capacityPm.getDouble("HPROPRIETEO")
    ricevutoteo_value = capacityPm.getDouble("RICEVUTOTEO")

    # Calcular FLAVOROTEO
    flavoroteo = hproprieteo_value + ricevutoteo_value

    # Actualizar el campo FLAVOROTEO en el Mbo correspondiente
    capacityPm.setValue("FLAVOROTEO", round(flavoroteo, 2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    #related_pm_mbo_set.save()

    # Calcular il valore di FLAVOROPLAN come HPROPRIEPLAN + RICEVUTOPLAN
    hproprieplan_value = capacityPm.getDouble("HPROPRIEPLAN")
    ricevutoplan_value = capacityPm.getDouble("RICEVUTOPLAN")

    # Calcular FLAVOROPLAN
    flavoroplan = hproprieplan_value + ricevutoplan_value

    # Update FLAVOROPLAN
    capacityPm.setValue("FLAVOROPLAN", round(flavoroplan,2), MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
    pmSet.save()
    pmSet.close()
    related_pm_mbo = related_pm_mbo_set.moveNext()
    
related_pm_mbo_set.save()
related_pm_mbo_set.close()