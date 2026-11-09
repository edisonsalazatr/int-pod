"""
X4CATCHILDASSETTYPE
The script set the rule and description when the target asset type has been set

Version history
1.0 - 27/03/2026:
	first implementation
"""
from psdi.util.logging import MXLoggerFactory
from com.terna.common.string import StringUtils;
from psdi.mbo import SqlFormat
from com.terna.common.maximo import MXUtils

# logs an INFO message 
def info(message, *args):
	# logs 
	log.info(StringUtils.format("[" + scriptName + "] " + message, args))
	
# get logger
log = MXLoggerFactory.getLogger("maximo.catalog")


# get the target asset type
relatedType = mbo.getString("tipologiatarget")

if StringUtils.isEmpty(relatedType):
	# reset rule id
	mbo.setValueNull("ruleid")
	# reset description
	mbo.setValueNull("description")
else:
	# get related asset type
	where = SqlFormat("catalogo = :0 and elemento_riferimento = :1 and elemento_target = :2")
	where.setObject(0, "UPPER", mbo.getString("type"))
	where.setObject(1, "UPPER", mbo.getString("tipologiaparent"))
	where.setObject(2, "UPPER", mbo.getString("tipologiatarget"))
	relatedAssetTypeSet = mbo.getMboSet("$relatedassetypes$", "X4_TIPOLOGIE_CORRELATE", where.format())
	relatedAssetType = relatedAssetTypeSet.getMbo(0)
	if relatedAssetType != None:
		info("found path for {0} -> {1}", mbo.getString("tipologiaparent"), mbo.getString("tipologiatarget"))
		# set rule id
		ruleSet = relatedAssetType.getMboSet("$rule$", "X4_RULES", "x4_tipologie_correlateid = :x4_tipologie_correlateid")
		info("found {0} rule(s)", ruleSet.count())
		if ruleSet.count() == 1:
			rule = ruleSet.getMbo(0)
			info("setting rule {0}", rule.getUniqueIDValue())
			mbo.setValue("ruleid", rule.getUniqueIDValue())
			# set description
			mbo.setValue("description", rule.getString("description"))
	

