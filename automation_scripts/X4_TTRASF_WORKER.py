from psdi.util.logging import MXLoggerFactory
from java.lang import System

logger = MXLoggerFactory.getLogger("maximo.terna")

ownerSet = None
wpLaborSet = None

startTime = System.currentTimeMillis()

try:

    # ========================================================
    # PARAMETRI RICEVUTI DAL LAUNCHER
    # ========================================================

    ownerObject = str(owner_object)
    ownerIdName = str(owner_id_name)
    ownerId = str(owner_id)
    
    sapid = str(destinazione)
    origine = str(origine)
    ttrasf = float(str(ttrasf))
    
    userInfo = user_info


    logger.error(
        "X4_TTRASF_WORKER - START - "
        "DESTINAZIONE=%s ORIGINE=%s TTRASF=%s"
        % (
            sapid,
            origine,
            ttrasf
        )
    )


    # ========================================================
    # 1. RIAPRO L'OWNER
    # ========================================================

    ownerSet = service.getMboSet(
        ownerObject,
        userInfo
    )

    ownerSet.setWhere(
        "%s = %s"
        % (
            ownerIdName,
            ownerId
        )
    )

    ownerSet.reset()

    ownerMbo = ownerSet.moveFirst()

    if ownerMbo is None:

        raise Exception(
            "Owner non trovato: %s=%s"
            % (
                ownerIdName,
                ownerId
            )
        )


    logger.error(
        "X4_TTRASF_WORKER - OWNER CARICATO"
    )


    # ========================================================
    # 2. PREPARO WHERE WPLABOR
    #
    # ========================================================

    origineSql = origine.replace(
        "'",
        "''"
    )

    sapidSql = sapid.replace(
        "'",
        "''"
    )

    whereClause = (
        "x4titris='%s' "
        "and x4datainiziorisorsa is not null "
        "and x4datafinerisorsa is not null "
        "and exists ("
        "select 1 "
        "from workorder wo "
        "where wo.wonum=wplabor.wonum "
        "and wo.istask=0 "
        "and wo.historyflag=0 "
        "and wo.x4aggregati=0 "
        "and wo.status in ('DA VALIDARE','APERTO') "
        "and substr(wo.x4sapidasset,1,7)='%s'"
        ")"
        % (
            origineSql,
            sapidSql
        )
    )


    # ========================================================
    # 3. WPLABOR VIENE APERTO DALL'OWNER
    #
    # ========================================================

    wpLaborSet = ownerMbo.getMboSet(
        "$WPLABORAP_BG$",
        "WPLABOR",
        whereClause
    )


    logger.error(
        "X4_TTRASF_WORKER - BEFORE moveFirst"
    )

    wpLabor = wpLaborSet.moveFirst()

    logger.error(
        "X4_TTRASF_WORKER - AFTER moveFirst"
    )


    # ========================================================
    # 4. MODIFICA WPLABOR
    # ========================================================

    count = 0

    while wpLabor is not None:

        wpLabor.setValue(
            "X4ORETRASF",
            ttrasf,
            11L
        )

        count += 1

        if count % 500 == 0:

            logger.error(
                "X4_TTRASF_WORKER - "
                "MODIFICATI=%s"
                % count
            )

        wpLabor = wpLaborSet.moveNext()


    logger.error(
        "X4_TTRASF_WORKER - "
        "FINE LOOP - RECORD=%s"
        % count
    )

    saveStart = System.currentTimeMillis()

    logger.error(
        "X4_TTRASF_WORKER - "
        "BEFORE SAVE - RECORD=%s"
        % count
    )

    wpLaborSet.save()

    saveTime = (
        System.currentTimeMillis()
        - saveStart
    )

    logger.error(
        "X4_TTRASF_WORKER - "
        "AFTER SAVE - "
        "RECORD=%s "
        "SAVE_TIME=%sms"
        % (
            count,
            saveTime
        )
    )


except Exception, e:

    logger.error(
        "X4_TTRASF_WORKER - ERROR - %s"
        % str(e)
    )

    raise


finally:

    if wpLaborSet is not None:
        try:
            wpLaborSet.close()
        except:
            pass

    if ownerSet is not None:
        try:
            ownerSet.close()
        except:
            pass


totalTime = (
    System.currentTimeMillis()
    - startTime
)

logger.error(
    "X4_TTRASF_WORKER - "
    "END - TOTAL_TIME=%sms"
    % totalTime
)