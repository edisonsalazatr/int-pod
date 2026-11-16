# X4GESTISCIEVENTOESEGUITO v. 26/01/2026
from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory
from java.util import Date
from java.lang import System, Exception as JavaException
from com.terna.common.exception import MXLiteralException, MXBaseException
from psdi.mbo import MboConstants

# -------------------- FUNZIONI DI UTILITA' --------------------
def safe_close(mboset, save=False):
    if not mboset:
        return
    saved_exc = None
    try:
        if save:
            mboset.save()
    except Exception as e:
        logger.error("%s Errore nel salvataggio: %s" % (SCRIPT, e))
        saved_exc = e
    try:
        mboset.close()
    except Exception as e:
        logger.error("%s Errore nella chiusura: %s" % (SCRIPT, e))
        if not saved_exc:
            saved_exc = e
    if saved_exc:
        raise saved_exc

def get_mbo_set(name, where, orderBy=None, discardable=False):
    s = SERVER.getMboSet(name, userInfo)
    if discardable:
        s.setFlag(MboConstants.NOACCESSCHECK, True)
        s.setFlag(MboConstants.DISCARDABLE, True)
    s.setWhere(where)
    if orderBy:
        s.setOrderBy(orderBy)
    s.reset()
    return s

def ensure_x4misureasset(assetnum, siteid, idmisura, idmisura_ext):
    xs = None
    try:
        where = (
            "ASSETNUM='%s' AND SITEID='%s' AND IDMISURA='%s' AND IDMISURA_EXT='%s'"
            % (assetnum, siteid, idmisura, idmisura_ext)
        )
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
    except:
        safe_close(xs)
        raise

def getVariabileErroreSet(assetnum, siteid, x4catmisureid):
    where = (
        "assetnum='%s' AND siteid='%s' AND x4catmisureid=%s"
        % (assetnum, siteid, x4catmisureid)
    )
    return get_mbo_set("X4VARIABILERRORE", where)

# -------------------- INIT --------------------
logger = MXLoggerFactory.getLogger("maximo.terna")
SERVER = MXServer.getMXServer()
SCRIPT = "[ X4GESTISCIEVENTOESEGUITO ]"
userInfo = mbo.getUserInfo()
errore = "ERRC000"
VarErrEx = service.invokeScript("X4VARERREX").get("VarErrEx")
lib = service.invokeScript("X4EVALCOND").get("X4EvalCondLib")(service)

# -------------------- ENTRY POINT --------------------
if mbo and not mbo.isNull("JPNUM") and mbo.isModified("STATUS") and mbo.getString("STATUS") == "ESEGUITO":

    start_ts = System.currentTimeMillis()
    jpnum = mbo.getString("JPNUM")
    assetnum = mbo.getString("ASSETNUM")
    siteid = mbo.getString("SITEID")

    misureSet = None
    try:
        where_misure = ("CALCOLO='GESTISCIEVENTOESEGUITO' AND X4CATMISUREID IN (SELECT X4CATMISUREID FROM X4CATMISUREPARAMETRI WHERE PARAMETER='%s')" % jpnum)
        misureSet = get_mbo_set("X4CATMISURE", where_misure, discardable=True)

        misura = misureSet.moveFirst()
        while misura:
            idmisura = misura.getLong("X4CATMISUREID")
            idmisura_ext = misura.getString("IDMISURA")

            # --- Applicabilità: salta il calcolo se la misura non si applica all'asset.
            #     Guardia: chiama lib.check SOLO se la misura ha condizioni (evita il load ASSETSPEC).
            #     Fail-open: se la verifica fallisce, log e procedo come applicabile (come in produzione). ---
            isApplicable = True
            cs = None
            try:
                cs = get_mbo_set("X4CARDMISURE", "X4CATMISUREID=%d" % idmisura, discardable=True)
                if cs is not None and not cs.isEmpty():
                    isApplicable = lib.check(userInfo, assetnum, "MISURA", idmisura_ext)
            except Exception as e:
                logger.error("%s Errore verifica applicabilita' misura %s: %s (procedo come applicabile)" % (SCRIPT, idmisura_ext, e))
                isApplicable = True
            finally:
                safe_close(cs)

            service.log("%s Applicabilita' asset %s misura %s: %s" % (SCRIPT, assetnum, idmisura_ext, isApplicable))

            if isApplicable:
                parameter_set = None
                try:
                    whereParams = ("X4CATMISUREID=%d AND CALCOLO_RESET='CALCOLO' AND PARAMETER_TYPE='VARIABILE'" % idmisura)
                    parameter_set = get_mbo_set("X4CATMISUREPARAMETRI", whereParams, discardable=True)

                    parameter = parameter_set.moveFirst()
                    while parameter:
                        variabile = parameter.getString("PARAMETER")
                        valore = None

                        try:
                            if variabile:
                                mset = None
                                try:
                                    clause = ("IDMISURA_EXT='%s' AND ASSETNUM='%s' AND SITEID='%s'" % (variabile, assetnum, siteid))
                                    mset = get_mbo_set("X4MISUREASSET", clause)
                                    if not mset.isEmpty():
                                        valore = mset.getMbo(0).getLong("VALORE")
                                finally:
                                    safe_close(mset)

                            mb_x, xs = ensure_x4misureasset(assetnum, siteid, idmisura, idmisura_ext)
                            try:
                                mb_x.setValue("VALORE", valore if valore is not None else 0)
                                mb_x.setValue("ULTIMARILEVAZIONE", Date())
                            finally:
                                safe_close(xs, save=True)

                        except VarErrEx as vve:
                            vs = None
                            try:
                                vs = getVariabileErroreSet(assetnum, siteid, idmisura)
                                errRec = vs.add()
                                errRec.setValue("siteid", siteid)
                                errRec.setValue("assetnum", assetnum)
                                errRec.setValue("x4catmisureid", idmisura)
                                errRec.setValue("errorcode", vve.getCode())
                                errRec.setValue("error", vve.getMessage())
                                safe_close(vs, save=True)
                            except:
                                safe_close(vs)

                        except Exception as e:
                            # gestione errore generico
                            msg = (
                                MXBaseException.getPlainMessage(e)
                                if isinstance(e, MXLiteralException)
                                else str(e)
                            )
                            vs = None
                            try:
                                vs = getVariabileErroreSet(assetnum, siteid, idmisura)
                                errRec = vs.add()
                                errRec.setValue("siteid", siteid)
                                errRec.setValue("assetnum", assetnum)
                                errRec.setValue("x4catmisureid", idmisura)
                                errRec.setValue("errorcode", errore)
                                errRec.setValue("error", msg)
                                safe_close(vs, save=True)
                            except:
                                safe_close(vs)

                        parameter = parameter_set.moveNext()
                finally:
                    safe_close(parameter_set)

            misura = misureSet.moveNext()
    finally:
        safe_close(misureSet)

    elapsed = (System.currentTimeMillis() - start_ts) / 1000.0
    logger.info("%s Tempo esecuzione: %.3f s" % (SCRIPT, elapsed))
    service.log("%s Tempo esecuzione: %.3f s" % (SCRIPT, elapsed))