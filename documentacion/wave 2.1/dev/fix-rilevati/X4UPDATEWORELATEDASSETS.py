from java.util import HashMap
from psdi.util.logging import MXLoggerFactory
from com.terna.common.string import StringUtils

# logs an INFO message
def info(message, *args):
	# logs
	if log is not None:
		if "wo" in globals() and wo != None:
			log.info(StringUtils.format("[X4UPDATEWORELATEDASSETS(" + wo.getString("wonum") + ")] " + message, args))
		else:
			log.info(StringUtils.format("[X4UPDATEWORELATEDASSETS] " + message, args))

def stringAttributeChanged(mbo, attribute):
	prevValue = ""
	try:
		prevValue = mbo.getMboValue(attribute).getPreviousValue().asString()
	except:
		prevValue = ""
	return prevValue != mbo.getString(attribute)

def intAttributeChanged(mbo, attribute):
	prevValue = 0
	try:
		prevValue = mbo.getMboValue(attribute).getPreviousValue().asInt()
	except:
		prevValue = 0
	return prevValue != mbo.getInt(attribute)



log = MXLoggerFactory.getLogger("maximo.catalog")
wo = mbo

info("evaluating related asset refresh for wo {0} status {1}", wo.getString("wonum"), wo.getString("status"))
statusChanged = stringAttributeChanged(wo, "status") and wo.getString("status") in ("APERTO", "SCHEDULATO")
jobPlanChanged = stringAttributeChanged(wo, "jpnum") or intAttributeChanged(wo, "pluscjprevnum")
info("changed status {0} jobplan {1}", statusChanged, jobPlanChanged)

if statusChanged or jobPlanChanged:
	info("invoking related asset refresh")
	context = HashMap()
	context.put("mbo", mbo)
	context.put("service", service)
	service.invokeScript("X4GETWORELATEDASSETS", context)
	
