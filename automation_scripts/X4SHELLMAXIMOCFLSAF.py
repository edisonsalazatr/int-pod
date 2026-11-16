from psdi.server import MXServer
from psdi.mbo import MboConstants
from java.util import HashMap
from psdi.util.logging import MXLoggerFactory
from java.lang import System, Exception as JavaException
logger = MXLoggerFactory.getLogger("maximo.terna")

service.log("==== Script X4SHELLMAXIMOCFLSAF START: Counting matching workorders ====")

if onadd or (onupdate and mbo.isModified("STATUS")):
    if mbo.isNew():
        service.log("Trigger detected: New Work "+str(mbo.getString("WONUM"))+" Order.")
    else:
        service.log("Trigger detected: "+str(mbo.getString("WONUM"))+" STATUS field was updated.")

    start_ts = System.currentTimeMillis()
    scriptName = "X4CORECFLSAF"
    wonum = mbo.getString("WONUM")
    jpnum = mbo.getString("JPNUM")
    status = mbo.getString("STATUS")
    assetnum = mbo.getString("ASSETNUM")
    userInfo = mbo.getUserInfo()
    context = HashMap()
    context.put("ctxwonum", wonum)
    context.put("ctxjpnum", jpnum)
    context.put("ctxstatus", status)
    context.put("ctxassetnum", assetnum)
    context.put("ctxuserInfo", userInfo)
    try:
        service.invokeScript(scriptName, context)
    except JavaException as je:
        logger.error("Error: '%s'" % str(je))
    except Exception as e:
        logger.error("Error: '%s'" % str(e))
else:
    service.log("Skip: Not a new Work Order  "+str(mbo.getString("WONUM"))+" and STATUS not modified.")

service.log("==== Script X4SHELLMAXIMOCFLSAF END ====")