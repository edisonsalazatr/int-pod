#X4DATAOPTEVENT v. 1.5
from psdi.server import MXServer
from java.util import Date, Calendar
from java.text import SimpleDateFormat
from com.terna.common.exception import MXLiteralException, MXBaseException
from psdi.util.logging import MXLoggerFactory
from psdi.mbo import MboConstants
from java.lang import System, Math, Exception as JavaException

def find_date_diff_days(from_date, to_date):
    if not from_date or not to_date:
        return None
    delta = to_date.getTime() - from_date.getTime()
    return int(Math.floor(delta / MILLIS_PER_DAY))

def batch_fetch_params(x4catmisureid):
    params = []
    mset = None
    try:
        where = "X4CATMISUREID=%d AND CALCOLO_RESET='CALCOLO'" % x4catmisureid
        mset = get_mbo_set("X4CATMISUREPARAMETRI", where)
        for i in range(mset.count()):
            params.append(mset.getMbo(i).getString("PARAMETER"))
    finally:
        safe_close(mset)
    return params

def safe_close(mboset, save=False):
    if mboset is None:
        return
    exc = None
    if save:
        try:
            mboset.save()
        except Exception as e:
            logger.error(script + " Errore nel salvataggio: %s" % e)
            exc = e
    try:
        mboset.close()
    except Exception as e:
        logger.error(script + "Errore nella chiusura: %s" % e)
        if exc is None:
            exc = e
    if exc:
        raise exc


def ensure_x4misureasset(assetnum, siteid, idmisura, idmisura_ext):
    xs = None
    try:
        where = "ASSETNUM='%s' AND SITEID='%s' AND IDMISURA='%s' AND IDMISURA_EXT='%s'" % (assetnum, siteid, idmisura, idmisura_ext)
        xs = get_mbo_set("X4MISUREASSET", where)
        if xs.count() > 0:
            return xs.getMbo(0), xs
        mb = xs.add()
        mb.setValue("SITEID", siteid)
        mb.setValue("ASSETNUM", assetnum)
        mb.setValue("IDMISURA", str(idmisura))
        mb.setValue("IDMISURA_EXT", idmisura_ext)
        mb.setValue("PRIMARILEVAZIONE", Date())
        mb.setValue("ULTIMARILEVAZIONE", Date())
        return mb, xs
    except Exception:
        safe_close(xs)
        raise


def getVariabileErroreSet(assetnum, siteid, x4catmisureid):
    where = "assetnum='%s' AND siteid='%s' AND x4catmisureid=%d" % (assetnum, siteid, x4catmisureid)
    vs = get_mbo_set("X4VARIABILERRORE", where)
    return vs


def get_mbo_set(name, where, maxrows=None, orderBy=None):
    try:
        s = SERVER.getMboSet(name, userInfo)
        s.setWhere(where)
        if orderBy is not None:
            s.setOrderBy(orderBy)
        if isinstance(maxrows, int) and maxrows > 0:
            s.setDBFetchMaxRows(maxrows)
        s.reset()
        return s
    except JavaException as je:
        raise

SERVER = MXServer.getMXServer()
date_format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
MILLIS_PER_DAY = 1000.0 * 60 * 60 * 24
CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR)
logger = MXLoggerFactory.getLogger("maximo.terna")
script = "[ X4DATAOPTEVENT ]"
userInfo = mbo.getUserInfo()
errore = "ERRCDO000"
#ENTRY POINT
if mbo:
    start_ts = System.currentTimeMillis()
    try:
        assetnum = mbo.getString("ASSETNUM")
        jpnum    = mbo.getString("JPNUM")
        siteid   = mbo.getString("SITEID")

        where_mis = (
            "CALCOLO='DATAOPT' AND X4CATMISUREID IN "
            "(SELECT X4CATMISUREID FROM X4CATMISUREPARAMETRI WHERE PARAMETER='%s' AND CALCOLO_RESET = 'CALCOLO')" % jpnum
        )

        x4catmisure_set = get_mbo_set("X4CATMISURE", where_mis)
        x4catmisure_set.setFlag(MboConstants.DISCARDABLE, True)
        try:
            misura = x4catmisure_set.moveFirst()
            while misura:
                try:
                    reset = misura.getLong("RESET")
                    idmisura = misura.getLong("X4CATMISUREID")
                    idmisura_ext = misura.getString("IDMISURA")
                    params = batch_fetch_params(idmisura)
                    
                    jpnum_list = "','".join(params)
                    delta = None
                    where_wo = (
                        "JPNUM IN ('%s') AND ASSETNUM='%s' " % (jpnum_list, assetnum)
                        + "AND STATUS IN ('APERTO') "
                        + "AND NVL(TARGCOMPDATE, X4DATAMODELLO) > SYSDATE"
                    )
                    wo_set = get_mbo_set("WORKORDER", where_wo, orderBy="NVL(TARGCOMPDATE, X4DATAMODELLO) ASC")
                    try:
                        if wo_set.count() > 0:
                            firstMbo = wo_set.getMbo(0)
                            nextDate = (firstMbo.getDate("TARGCOMPDATE") or firstMbo.getDate("X4DATAMODELLO"))
                            if nextDate:
                                delta = find_date_diff_days(Date(), nextDate)
                            else:
                                msg = "Il record non ha TARGCOMPDATE valido"
                                logger.error(msg)
                                
                        if delta is not None and delta > 0 and reset > 0:
                            delta = 0

                        mb_x, xs = ensure_x4misureasset(assetnum, siteid, idmisura, idmisura_ext)
                        try:
                            if delta is not None:
                                mb_x.setValue("VALORE", delta)
                            else:
                                mb_x.setValue("VALORE", 100000)
                            mb_x.setValue("ULTIMARILEVAZIONE", Date())
                        finally:
                            safe_close(xs, save=True)
                    finally:
                        safe_close(wo_set)

                except (MXLiteralException, JavaException, Exception) as e:
                    msg = MXBaseException.getPlainMessage(e) if isinstance(e, MXLiteralException) else str(e)
                    logger.error(script + " Error %s in asset %s: %s" % (idmisura_ext, assetnum, msg))
                    vs = getVariabileErroreSet(assetnum, siteid, idmisura)
                    try:
                        errRec = vs.add()
                        errRec.setValue("siteid",    siteid)
                        errRec.setValue("assetnum",  assetnum)
                        errRec.setValue("x4catmisureid", idmisura)
                        errRec.setValue("errorcode", errore)
                        errRec.setValue("error",     msg)
                        safe_close(vs, save=True)
                    except:
                        safe_close(vs)
                finally:
                    misura = x4catmisure_set.moveNext()
        finally:
            safe_close(x4catmisure_set)

    except Exception as outer_e:
        logger.error(script + " Errore principale nello script: %s" % outer_e)

    elapsed = (System.currentTimeMillis() - start_ts) / 1000.0
    logger.info(script + " Tempo esecuzione: '%s' s" % str(elapsed))
    service.log(script + " Tempo esecuzione: '%s' s" % str(elapsed))