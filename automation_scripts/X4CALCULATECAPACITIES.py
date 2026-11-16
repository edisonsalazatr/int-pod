from psdi.mbo import MboConstants
from psdi.server import MXServer
from java.text import SimpleDateFormat


def calculate_capacity_fields(mbo):
    """
    Calcola Capacity Erosa e Residua per un Work Order Serbatoio.
    """
    try:
        if mbo.getInt("X4SERBATOIO") != 1:
            return
    
        effort_fields = {
            "OPERAIO": ("LABORHRS","X4EFFORTO", "X4CAPACITYEROSAO", "X4CAPACITYRESIDUAO", "X4WOSERBOPWP"),
            "IMPIEGAT": ("LABORHRS","X4EFFORTI", "X4CAPACITYEROSAI", "X4CAPACITYRESIDUAI", "X4WOSERBIMPWP"),
            "QUADRO": ("LABORHRS","X4EFFORTQ", "X4CAPACITYEROSAQ", "X4CAPACITYRESIDUAQ", "X4WOSERBQUAWP")
        }
    
        # Itera sui tipi di lavoratori
        for craft, fields in effort_fields.items():
            effort_field, effortodl, capacity_erosa_field, capacity_residua_field, relation_field = fields
            total_effort = 0.0
    
            # Recupera i Work Orders correlati tramite la relazione
            related_wo_set = mbo.getMboSet(relation_field)
            related_wo_set.reset()
            
            # Itera sui Work Orders trovati
            if not related_wo_set.isEmpty():
                related_wo_mbo = related_wo_set.moveFirst()
                #service.error("Ciao: ", str(related_wo_set.count()))
                while related_wo_mbo:
                   
                    total_effort += (related_wo_mbo.getDouble(effort_field)+related_wo_mbo.getDouble("X4ORETRASF"))*related_wo_mbo.getInt("QUANTITY")
                    related_wo_mbo = related_wo_set.moveNext()
                
                mbo.setValue(capacity_erosa_field, float(total_effort), MboConstants.NOACCESSCHECK)
                current_effort = mbo.getDouble(effortodl)
                capacity_residua = float(current_effort) - float(total_effort)
                #service.error("Caio: ", str(capacity_residua))
                #service.error("Valori: ", "Current-effort: {} and total_effort: {}".format(current_effort, total_effort))
                mbo.setValue(capacity_residua_field, float(capacity_residua), MboConstants.NOACCESSCHECK)
            else:
                #service.error("TEST",str(mbo.getString("WONUM")))
                current_effort = mbo.getDouble(effortodl)
                mbo.setValue(capacity_erosa_field,0,MboConstants.NOACCESSCHECK)
                mbo.setValue(capacity_residua_field, current_effort,MboConstants.NOACCESSCHECK)
    
            related_wo_set.close()

    except Exception as e:
        service.error("Errore nel calcolo della capacity erosa e residua", str(e))


def propagate_effort_change(mbo):
    """
    Propaga le modifiche degli Effort a tutti i Work Orders correlati.
    """
    try:
        wpLaborSet=mbo.getMboSet("WPLABOR")
        if not wpLaborSet.isEmpty():
            for i in range (0,wpLaborSet.count()):
                wpLabor=wpLaborSet.getMbo(i)
                modified_wonum = mbo.getString("WONUM")
                titris=wpLabor.getString("X4TITRIS")
                if not wpLabor.isNull("X4DATAINIZIORISORSA") and not wpLabor.isNull("X4DATAFINERISORSA") and not wpLabor.isNull("X4TITRIS"):
                    datainizio_str = date_format.format(wpLabor.getDate("X4DATAINIZIORISORSA"))
                    datafine_str = date_format.format(wpLabor.getDate("X4DATAFINERISORSA"))
                    titris_old=wpLabor.getMboValue("X4TITRIS").getInitialValue().asString()
                    if onupdate:
                        datainizio_str_old = date_format.format(wpLabor.getMboValue("X4DATAINIZIORISORSA").getInitialValue().asDate())
                        datafine_str_old = date_format.format(wpLabor.getMboValue("X4DATAFINERISORSA").getInitialValue().asDate())
                    
                    effort_fields = {
                        "OPERAIO": "X4WOSERBOP",
                        "IMPIEGAT": "X4WOSERBIMP",
                        "QUADRO": "X4WOSERBQUA"
                    }
            
                    # Recupera tutti i Work Orders serbatoio
                    mxserver = MXServer.getMXServer()
                    user_info = mbo.getUserInfo()
                    serbatoio_set = mxserver.getMboSet("WORKORDER", user_info)
                    serbatoio_set.setWhere("""
                    SITEID= '{}' AND WONUM= '{}' AND
                    X4COIN = '{}'
                    AND X4SERBATOIO = 1
                    AND X4BURKS = '{}'
                    AND REPORTDATE < TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
                    AND wonum IN (
                        SELECT wonum
                        FROM wplabor
                        WHERE X4TITRIS = '{}'
                        AND X4DATAFINERISORSA >= TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
                        AND X4DATAINIZIORISORSA <= TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
                    )
                    """.format(mbo.getString("SITEID"),mbo.getString("WONUM"),X4COIN, X4BURKS, report_date_str,titris, datafine_str, datainizio_str))
                    serbatoio_set.reset()
                    if not serbatoio_set.isEmpty():
                        serbatoio_mbo = serbatoio_set.moveFirst()
                        while serbatoio_mbo:
                            for craft, relation_field in effort_fields.items():
                             
                                related_wo_set = serbatoio_mbo.getMboSet(relation_field)
                                related_wo_set.reset()
                
                                # Verifica se il Work Order modificato è nella lista
                                related_wo_mbo = related_wo_set.moveFirst()
                                while related_wo_mbo:
                                    if related_wo_mbo.getString("WONUM") == modified_wonum:
                                        # Ricalcola i campi del Work Order serbatoio
                                        calculate_capacity_fields(serbatoio_mbo)
                                        break
                                    related_wo_mbo = related_wo_set.moveNext()
                                #related_wo_set.save()
                                related_wo_set.close()
                
                            serbatoio_mbo = serbatoio_set.moveNext()
                    serbatoio_set.save()
                    serbatoio_set.close()
                    
                    if onupdate:
                        if titris!=titris_old or datainizio_str_old!=datainizio_str or datafine_str_old!=datafine_str or X4COIN!=X4COIN_OLD:
                            #service.error("TEST3",str(X4COIN_OLD)+","+str(X4COIN))
                            serbatoio_set = mxserver.getMboSet("WORKORDER", user_info)
                            serbatoio_set.setWhere("""
                            SITEID= '{}' AND WONUM= '{}' AND
                            X4COIN = '{}'
                            AND X4SERBATOIO = 1
                            AND X4BURKS = '{}'
                            AND REPORTDATE < TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
                            AND wonum IN (
                                SELECT wonum
                                FROM wplabor
                                WHERE X4TITRIS = '{}'
                                AND X4DATAFINERISORSA >= TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
                                AND X4DATAINIZIORISORSA <= TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
                            )
                            """.format(mbo.getString("WONUM"),mbo.getString("SITEID"),X4COIN_OLD, X4BURKS, report_date_str, titris_old, datafine_str_old, datafine_str_old))
                            serbatoio_set.reset()
                            if not serbatoio_set.isEmpty():
                                serbatoio_mbo = serbatoio_set.moveFirst()
                                while serbatoio_mbo:
                                    #service.error("TEST2",str(serbatoio_mbo.getString("WONUM")))
                                    for craft, relation_field in effort_fields.items():
            
                                        related_wo_set = serbatoio_mbo.getMboSet(relation_field)
                                        related_wo_set.reset()
                        
                                        # Verifica se il Work Order modificato è nella lista
                                        related_wo_mbo = related_wo_set.moveFirst()
                                        while related_wo_mbo:
                                            if related_wo_mbo.getString("WONUM") == modified_wonum:
                                                
                                                # Ricalcola i campi del Work Order serbatoio
                                                calculate_capacity_fields(serbatoio_mbo)
                                                break
                                            related_wo_mbo = related_wo_set.moveNext()
                                        #related_wo_set.save()
                                        related_wo_set.close()
                        
                                    serbatoio_mbo = serbatoio_set.moveNext()
                            serbatoio_set.save()
                            serbatoio_set.close()
        wpLaborSet.close()

    except Exception as e:
        service.error("Errore nella propagazione delle modifiche agli Effort", str(e))

def propagate_effort_change_wplabor(mbo):
    """
    Propaga le modifiche degli Effort a tutti i Work Orders correlati.
    """
    try:
        modified_wonum = mbo.getString("WONUM")
        titris=mbo.getString("X4TITRIS")
        datainizio_str = date_format.format(mbo.getDate("X4DATAINIZIORISORSA"))
        datafine_str = date_format.format(mbo.getDate("X4DATAFINERISORSA"))
        titris_old=mbo.getMboValue("X4TITRIS").getInitialValue().asString()
        datainizio_str_old = date_format.format(mbo.getMboValue("X4DATAINIZIORISORSA").getInitialValue().asDate())
        datafine_str_old = date_format.format(mbo.getMboValue("X4DATAFINERISORSA").getInitialValue().asDate())

        effort_fields = {
            "OPERAIO": "X4WOSERBOP",
            "IMPIEGAT": "X4WOSERBIMP",
            "QUADRO": "X4WOSERBQUA"
        }

        # Recupera tutti i Work Orders serbatoio
        mxserver = MXServer.getMXServer()
        user_info = mbo.getUserInfo()
        serbatoio_set = mxserver.getMboSet("WORKORDER", user_info)
        serbatoio_set.setWhere("""
            SITEID= '{}' AND WONUM= '{}' AND
            X4COIN = '{}'
            AND X4SERBATOIO = 1
            AND X4BURKS = '{}'
            AND REPORTDATE < TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
            AND wonum IN (
                SELECT wonum
                FROM wplabor
                WHERE X4TITRIS = '{}'
                AND X4DATAFINERISORSA >= TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
                AND X4DATAINIZIORISORSA <= TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
            )
        """.format(mbo.getString("WONUM"),mbo.getString("SITEID"),X4COIN, X4BURKS,report_date_str, titris, datafine_str, datainizio_str ))
        serbatoio_set.reset()
        if not serbatoio_set.isEmpty():
            serbatoio_mbo = serbatoio_set.moveFirst()
            while serbatoio_mbo:
                for craft, relation_field in effort_fields.items():
                 
                    related_wo_set = serbatoio_mbo.getMboSet(relation_field)
                    related_wo_set.reset()
    
                    # Verifica se il Work Order modificato è nella lista
                    related_wo_mbo = related_wo_set.moveFirst()
                    while related_wo_mbo:
                        if related_wo_mbo.getString("WONUM") == modified_wonum:
                            # Ricalcola i campi del Work Order serbatoio
                            calculate_capacity_fields(serbatoio_mbo)
                            break
                        related_wo_mbo = related_wo_set.moveNext()
                    #related_wo_set.save()
                    related_wo_set.close()
    
                serbatoio_mbo = serbatoio_set.moveNext()
        serbatoio_set.save()
        serbatoio_set.close()
        
        if titris!=titris_old or datainizio_str_old!=datainizio_str or datafine_str_old!=datafine_str:
            #service.error("TEST3",str(titris)+","+str(titris_old))
            serbatoio_set = mxserver.getMboSet("WORKORDER", user_info)
            serbatoio_set.setWhere("""
            SITEID= '{}' AND WONUM= '{}' AND
            X4COIN = '{}'
            AND X4SERBATOIO = 1
            AND X4BURKS = '{}'
            AND REPORTDATE < TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
            AND wonum IN (
                SELECT wonum
                FROM wplabor
                WHERE X4TITRIS = '{}'
                AND X4DATAFINERISORSA >= TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
                AND X4DATAINIZIORISORSA <= TO_TIMESTAMP('{}', 'YYYY-MM-DD HH24:MI:SS')
            )
            """.format(mbo.getString("WONUM"),mbo.getString("SITEID"),X4COIN, X4BURKS, report_date_str,titris_old, datafine_str_old, datafine_str_old))
            serbatoio_set.reset()
            if not serbatoio_set.isEmpty():
                serbatoio_mbo = serbatoio_set.moveFirst()
                while serbatoio_mbo:
                    #service.error("TEST2",str(serbatoio_mbo.getString("WONUM")))
                    for craft, relation_field in effort_fields.items():

                        related_wo_set = serbatoio_mbo.getMboSet(relation_field)
                        related_wo_set.reset()
        
                        # Verifica se il Work Order modificato è nella lista
                        related_wo_mbo = related_wo_set.moveFirst()
                        while related_wo_mbo:
                            if related_wo_mbo.getString("WONUM") == modified_wonum:
                                # Ricalcola i campi del Work Order serbatoio
                                calculate_capacity_fields(serbatoio_mbo)
                                break
                            related_wo_mbo = related_wo_set.moveNext()
                        if not related_wo_mbo:
                            calculate_capacity_fields(serbatoio_mbo)
                        related_wo_set.save()
                        related_wo_set.close()
        
                    serbatoio_mbo = serbatoio_set.moveNext()
            serbatoio_set.save()
            serbatoio_set.close()

    except Exception as e:
        service.error("Errore nella propagazione delle modifiche agli Effort", str(e))

try:
    if launchPoint=="X4CALCULATECAPACITIES":
        if not mbo.isNull("OWNERGROUP") and not mbo.isNull("X4COIN") and not mbo.isNull("TARGSTARTDATE") and not mbo.isNull("TARGCOMPDATE") and (mbo.getString("STATUS")=="APERTO" or mbo.getString("STATUS")=="DA VALIDARE" or mbo.getString("STATUS")=="BOZZA"):
            OWNERGROUP = mbo.getString("OWNERGROUP")
            X4COIN = mbo.getString("X4COIN")
            X4COIN_OLD=mbo.getMboValue("X4COIN").getInitialValue().asString()
            X4BURKS = mbo.getString("X4BURKS")
            REPORTDATE = mbo.getDate("REPORTDATE")
            date_format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            report_date_str = date_format.format(REPORTDATE)
            
            TARGSTARTDATE = mbo.getDate("TARGSTARTDATE")
            date_format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            start_date_str = date_format.format(TARGSTARTDATE)
            
            TARGCOMPDATE = mbo.getDate("TARGCOMPDATE")
            date_format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            comp_date_str = date_format.format(TARGCOMPDATE)
            if mbo.getString("STATUS") != "CHIUSO": 
                if mbo.getInt("X4SERBATOIO") == 1:
                    calculate_capacity_fields(mbo)
            
                if mbo.isModified("X4EFFORTO") or mbo.isModified("X4EFFORTI") or mbo.isModified("X4EFFORTQ") or mbo.isModified("X4BURKS") or mbo.isModified("X4COIN"):
                    #mbo.getThisMboSet().save()
                    propagate_effort_change(mbo)
            
                    #mbo.getThisMboSet().save()
    elif launchPoint=="X4CALCULATECAPACITIES_WPLAB":
        if mbo.isModified("LABORHRS") or mbo.isModified("X4TITRIS") or mbo.isModified("X4DATAINIZIORISORSA") or mbo.isModified("X4DATAFINERISORSA") or mbo.isModified("QUANTITY") or mbo.isModified("CRAFT"):
            woSet=mbo.getMboSet("WORKORDER")
            X4COIN=woSet.getMbo(0).getString("X4COIN")
            X4BURKS = woSet.getMbo(0).getString("X4BURKS")
            REPORTDATE = woSet.getMbo(0).getDate("REPORTDATE")
            date_format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            report_date_str = date_format.format(REPORTDATE)
            #mbo.getThisMboSet().save()
            woSet.close()
            propagate_effort_change_wplabor(mbo)
            #mbo.getThisMboSet().save()

except Exception as e:
    service.error("Errore nello script principale", str(e))