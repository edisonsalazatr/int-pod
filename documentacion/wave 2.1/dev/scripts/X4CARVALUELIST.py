"""
X4CARVALUELIST
The script set domain and the look up for the attributes of a DIFFORMITA/INTERFERENZE
according to the attribute set 

Version history
1.0 - 20/02/2026:
    first implementation
"""
from psdi.util.logging import MXLoggerFactory
from com.terna.common.string import StringUtils
from com.terna.common.maximo import MXUtils
from psdi.mbo import SqlFormat
from java.util import Arrays

# logs an INFO message 
def info(message, *args):
	# get logger
	log = MXLoggerFactory.getLogger("maximo.catalog")
	# logs 
	log.info(StringUtils.format("[" + scriptName + "] " + message, args))

# @return the class structure id for the given asset type, None if not set
def getClassStructureId(assetType):
	# class structure id for the given asset type, will be None if not set
	classStructureId = None
	# get the catalog
	catalogMbo = mbo.getOwner()
	# get related asset types
	assetTypeSet = catalogMbo.getMboSet("X4CATCHILD")
	# search the required asset type
	i = 0
	assetTypeMbo = assetTypeSet.getMbo(0)
	while assetTypeMbo != None:
		if assetTypeMbo.getString("tipologiatarget") == assetType:
			# asset type found
			if not assetTypeMbo.isNull("classstructureid"):
				# class structure id set for the asset type
				classStructureId = assetTypeMbo.getString("classstructureid")
			break
		i = i + 1
		assetTypeMbo = assetTypeSet.getMbo(i)
	# return the class structure id
	return classStructureId
	
# get the asset type 
assetType = mbo.getString("tipologiaasset")
# get the attribute type 
attributeType = mbo.getString("attributetype")
# get the attribute type 
attributeName = mbo.getString("attribute")
info("setting domain for asset type {0} attribute type {1} attribute {2}", assetType, attributeType, attributeName)

if StringUtils.isEmpty(assetType) or StringUtils.isEmpty(attributeType) or StringUtils.isEmpty(attributeName):
	# need all the above fields set
	service.error("x3terna", "ErrorMsg", ["Non tutti i valori necessari sono stati compilati"])

relationObject = "X4CARVALUELIST"
listMboSet = MXUtils.getMboSet("X4CARVALUELIST")
srcKeys = Arrays.asList(["value"])
targetKeys = Arrays.asList(["value"])
listWhere = None
relationWhere = None

if attributeType == "C":
	# this attribute refers to a class specification
	# check if the class structure has been set for the asset type
	info("getting class structure id for asset type {0}", assetType)
	classStructureId = getClassStructureId(assetType)
	info("class structure id is {0}", classStructureId)
	
	# set the list where
	if classStructureId == None:
		# no class structure id set
		sqlFormat = SqlFormat(mbo, "attributetype = :attributetype and attribute = :attribute and classstructureid in (select min(classstructureid) from classstructure c where c.x4elemento = :tipologiaasset)")	
		where = sqlFormat.format()
	else:
		# class structure id set
		sqlFormat = SqlFormat(mbo, "attributetype = :attributetype and attribute = :attribute and classstructureid = :0")	
		sqlFormat.setObject(0, "UPPER", classStructureId)
		where = sqlFormat.format()
elif attributeType == "A":
	# this attribute refers to an asset attribute
	# get the attribute definition in order to figure out if the attribute value must be verified or not
	attributeDefSet = mbo.getMboSet("X4CARATTRIBUTE")
	attributeDefSet.reset()
	attributeDef = attributeDefSet.getMbo(0)
	if attributeDef != None and attributeDef.getBoolean("novalidation"):
		# the value must not be validated
		where = "1=2"
	else:
		# the value must be validated
		# set the list MBO set
		sqlFormat = SqlFormat(mbo, "attributetype = :attributetype and attribute = :attribute")	
		where = sqlFormat.format()
else:
	service.error("x3terna", "ErrorMsg", [StringUtils.format("Il tipo valore {0} per l'attributo {1} non e' supportato", attributeType, attributeName)])

info("where {0}", where)
listMboSet.setWhere(where)