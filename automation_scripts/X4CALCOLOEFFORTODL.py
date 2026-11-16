from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory

'''
Script su oggetto WPLABOR.
Launch point Object Save: After Commit.

Aggiorna:
- X4EFFORTO per CRAFT OPERAIO
- X4EFFORTI per CRAFT IMPIEGAT
- X4EFFORTQ per CRAFT QUADRO
- X4TTRASF

L'aggiornamento viene eseguito sia sul WORKORDER figlio sia sul padre.
Il padre viene riletto e salvato dopo il figlio.
'''

logger = MXLoggerFactory.getLogger("maximo.script")
mxs = MXServer.getMXServer()
sysui = mxs.getSystemUserInfo()

wo_set = None
wo_parent_set = None


def sql_escape(value):
    if value is None:
        return ""
    return str(value).replace("'", "''")


def get_effort_field(craft):
    craft_value = "" if craft is None else str(craft).strip().upper()

    if craft_value == "OPERAIO":
        return "X4EFFORTO"

    if craft_value == "IMPIEGAT":
        return "X4EFFORTI"

    if craft_value == "QUADRO":
        return "X4EFFORTQ"

    return None


def get_previous_double(attribute):
    try:
        return mbo.getMboValue(attribute).getPreviousValue().asDouble()
    except:
        return 0.0


def get_previous_int(attribute):
    try:
        return mbo.getMboValue(attribute).getPreviousValue().asInt()
    except:
        return 0


def add_effort_delta(delta_map, field_name, delta):
    if field_name is None:
        return

    if field_name in delta_map:
        delta_map[field_name] = delta_map[field_name] + delta
    else:
        delta_map[field_name] = delta


def apply_deltas(target_mbo, effort_deltas, transfer_delta):
    for field_name in effort_deltas:
        current_value = target_mbo.getDouble(field_name)
        target_mbo.setValue(field_name, current_value + effort_deltas[field_name], 11L)

    if transfer_delta != 0:
        target_mbo.setValue("X4TTRASF", target_mbo.getDouble("X4TTRASF") + transfer_delta, 11L)


try:
    wonum = mbo.getString("WONUM")
    siteid = mbo.getString("SITEID")
    craft = mbo.getString("CRAFT")

    logger.debug(
        "X4EFFORT start WPLABORID={} WONUM={} SITEID={} CRAFT={} "
        "onadd={} onupdate={} ondelete={}".format(
            mbo.getString("WPLABORID"),
            wonum,
            siteid,
            craft,
            onadd,
            onupdate,
            ondelete
        )
    )

    # After Commit: rileggo sempre il WORKORDER da database.
    wo_set = mxs.getMboSet("WORKORDER", sysui)
    wo_set.setWhere(
        "WONUM='{}' AND SITEID='{}'".format(
            sql_escape(wonum),
            sql_escape(siteid)
        )
    )
    wo_set.reset()

    wo = wo_set.moveFirst()

    if wo is None:
        logger.error(
            "X4EFFORT: WORKORDER non trovato. WONUM={} SITEID={}".format(
                wonum,
                siteid
            )
        )

    else:
        historyflag = wo.getBoolean("HISTORYFLAG")
        x4aggregati = wo.getBoolean("X4AGGREGATI")
        parent_wonum = None if wo.isNull("PARENT") else wo.getString("PARENT")

        effort_deltas = {}
        transfer_delta = 0.0
        execute_update = False

        current_quantity = mbo.getInt("QUANTITY")
        current_laborhrs = mbo.getDouble("LABORHRS")
        current_transfer = mbo.getDouble("X4ORETRASF")
        current_effort_field = get_effort_field(craft)

        # ------------------------------------------------------------
        # INSERIMENTO
        # ------------------------------------------------------------
        if onadd and not historyflag and not x4aggregati and mbo.isNull("JOBLABORID"):
            execute_update = True

            add_effort_delta(
                effort_deltas,
                current_effort_field,
                current_laborhrs * current_quantity
            )

            transfer_delta = current_transfer * current_quantity

        # ------------------------------------------------------------
        # CANCELLAZIONE
        # ------------------------------------------------------------
        elif ondelete and not historyflag:
            execute_update = True

            add_effort_delta(
                effort_deltas,
                current_effort_field,
                -(current_laborhrs * current_quantity)
            )

            transfer_delta = -(current_transfer * current_quantity)

        # ------------------------------------------------------------
        # MODIFICA
        # ------------------------------------------------------------
        elif onupdate and not historyflag:
            craft_modified = mbo.isModified("CRAFT")
            laborhrs_modified = mbo.isModified("LABORHRS")
            quantity_modified = mbo.isModified("QUANTITY")
            transfer_modified = mbo.isModified("X4ORETRASF")

            if craft_modified or laborhrs_modified or quantity_modified or transfer_modified:
                execute_update = True

                previous_quantity = get_previous_int("QUANTITY")
                previous_laborhrs = get_previous_double("LABORHRS")
                previous_transfer = get_previous_double("X4ORETRASF")

                previous_total_effort = previous_laborhrs * previous_quantity
                current_total_effort = current_laborhrs * current_quantity

                previous_total_transfer = previous_transfer * previous_quantity
                current_total_transfer = current_transfer * current_quantity

                transfer_delta = current_total_transfer - previous_total_transfer

                if craft_modified:
                    previous_craft = mbo.getMboValue("CRAFT").getPreviousValue().asString()
                    previous_effort_field = get_effort_field(previous_craft)

                    add_effort_delta(
                        effort_deltas,
                        previous_effort_field,
                        -previous_total_effort
                    )

                    add_effort_delta(
                        effort_deltas,
                        current_effort_field,
                        current_total_effort
                    )

                else:
                    add_effort_delta(
                        effort_deltas,
                        current_effort_field,
                        current_total_effort - previous_total_effort
                    )

        # ------------------------------------------------------------
        # SALVATAGGIO FIGLIO
        # ------------------------------------------------------------
        if execute_update:
            logger.debug(
                "X4EFFORT figlio={} parent={} effort_deltas={} transfer_delta={}".format(
                    wonum,
                    parent_wonum,
                    str(effort_deltas),
                    transfer_delta
                )
            )

            apply_deltas(wo, effort_deltas, transfer_delta)
            wo_set.save()

            logger.debug(
                "X4EFFORT: WORKORDER figlio {} salvato".format(
                    wonum
                )
            )

            # --------------------------------------------------------
            # SALVATAGGIO PADRE
            # Il padre viene aperto dopo il save del figlio e salvato
            # per ultimo, così eventuali action attivate dal figlio
            # non possono riscrivere successivamente il valore.
            # --------------------------------------------------------
            if parent_wonum is not None and str(parent_wonum).strip() != "":
                wo_parent_set = mxs.getMboSet("WORKORDER", sysui)
                wo_parent_set.setWhere(
                    "WONUM='{}' AND SITEID='{}'".format(
                        sql_escape(parent_wonum),
                        sql_escape(siteid)
                    )
                )
                wo_parent_set.reset()

                wo_parent = wo_parent_set.moveFirst()

                if wo_parent is None:
                    logger.error(
                        "X4EFFORT: padre non trovato. PARENT={} SITEID={}".format(
                            parent_wonum,
                            siteid
                        )
                    )

                else:
                    logger.debug(
                        "X4EFFORT padre={} effort_deltas={} transfer_delta={}".format(
                            parent_wonum,
                            str(effort_deltas),
                            transfer_delta
                        )
                    )

                    apply_deltas(wo_parent, effort_deltas, 0)
                    wo_parent_set.save()

                    logger.debug(
                        "X4EFFORT: WORKORDER padre {} salvato".format(
                            parent_wonum
                        )
                    )

except Exception as e:
    logger.error(
        "X4EFFORT errore WPLABORID={} WONUM={} SITEID={}: {}".format(
            mbo.getString("WPLABORID"),
            mbo.getString("WONUM"),
            mbo.getString("SITEID"),
            str(e)
        )
    )
    raise

finally:
    try:
        if wo_parent_set is not None:
            wo_parent_set.close()
    except:
        pass

    try:
        if wo_set is not None:
            wo_set.close()
    except:
        pass