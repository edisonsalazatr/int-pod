"""
X4CARVALUEVALIDATE
The script validate attribute's value of a DIFFORMITA/INTERFERENZE.
Cannot be empty if all the other line attributes have been set and the line cannot
be duplicated.

Version history
1.0 - 20/02/2026:
	first implementation
"""
from psdi.util.logging import MXLoggerFactory
from com.terna.common.string import StringUtils
from com.terna.common.maximo import MXUtils
from psdi.mbo import SqlFormat
from java.text import SimpleDateFormat
from java.text import ParsePosition
from psdi.mbo import MboConstants
from com.terna.common.date import DateUtils

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
	
	
# validate using the validation where	
def queryValidate(validationWhere, classStructureId):
	sqlFormat = SqlFormat(mbo, validationWhere)	
	if classStructureId != None:
		sqlFormat.setObject(0, "UPPER", classStructureId)
	where = sqlFormat.format()
	valueListSet = MXUtils.getMboSet("X4CARVALUELIST")
	valueListSet.setWhere(where)
	if valueListSet.count() == 0:
		service.error("x3terna", "ErrorMsg", [StringUtils.format("Il valore {0} per l'attributo {1} non e' valido", attributeValue, attributeName)])
	valueListSet.close()

# validate date
def dateValidate(value):
	dateFormat = SimpleDateFormat("dd/MM/yyyy")
	p = ParsePosition(0)
	d = dateFormat.parse(value, p)
	if d == None or p.getIndex() != len(value):
		service.error("x3terna", "ErrorMsg", ["Inserire la data nel formato gg/mm/aaaa"])
	# correct year and force the java representation
	if DateUtils.getYear(d) < 100:
		d = DateUtils.addYears(d, 2000)
	elif DateUtils.getYear(d) < 1000:
		d = DateUtils.addYears(d, 1000)	
	mbo.setValue("value", dateFormat.format(d), MboConstants.NOVALIDATION_AND_NOACTION)

	
# get the asset type
assetType = mbo.getString("tipologiaasset")
# get the attribute type
attributeType = mbo.getString("attributetype")
# get the attribute type
attributeName = mbo.getString("attribute")
# get the attribute value
attributeValue = mbo.getString("value")

# check empty value
if StringUtils.isEmpty(attributeValue) and not StringUtils.isEmpty(assetType) and not StringUtils.isEmpty(attributeType) and not StringUtils.isEmpty(attributeName):
	service.error("x3terna", "ErrorMsg", [StringUtils.format("Specificare il valore per l'attributo {0}", attributeName)])

# check the current line does not duplicate any other line 
currentSet = mbo.getThisMboSet()
i = 0
otherMbo = currentSet.getMbo(0)
while otherMbo:
	# skip the current MBO
	if otherMbo.getUniqueIDValue() != mbo.getUniqueIDValue():
		# check the line has the same settings
		if otherMbo.getString("tipologiaasset") == assetType and otherMbo.getString("attributetype") == attributeType and otherMbo.getString("attribute") == attributeName and otherMbo.getString("value") == attributeValue:
			# cannot have two lines with same setting
			service.error("x3terna", "ErrorMsg", [StringUtils.format("Il valore {0} e' gia' presente per l'attributo {1} per l'elemento {2}",attributeValue,attributeName, assetType)])
	i += 1
	otherMbo = currentSet.getMbo(i)


if attributeType == "C":
	# this attribute refers to a class specification
	# check if the class structure has been set for the asset type
	info("getting class structure id for asset type {0}", assetType)
	classStructureId = getClassStructureId(assetType)
	info("class structure id is {0}", classStructureId)
	
	# set the validation where
	if classStructureId == None:
		# no class structure id set
		validationWhere = "value = :value and attributetype = :attributetype and attribute = :attribute and classstructureid in (select min(classstructureid) from classstructure c where c.x4elemento = :tipologiaasset)"
	else:
		# class structure id set
		validationWhere = "value = :value and attributetype = :attributetype and attribute = :attribute and classstructureid = :0"
	# validate
	queryValidate(validationWhere, classStructureId)
elif attributeType == "A":
	# this attribute refers to an asset attribute
	# get the attribute definition in order to figure out if the attribute value must be verified or not
	attributeDefSet = mbo.getMboSet("X4CARATTRIBUTE")
	attributeDefSet.reset()
	attributeDef = attributeDefSet.getMbo(0)
	if attributeDef != None:
		if attributeDef.getString("datatype") == "DATE":
			# validate
			dateValidate(attributeValue)
		elif not attributeDef.getBoolean("novalidation"):
			# the value must be validated
			validationWhere = "value = :value and attributetype = :attributetype and attribute = :attribute"
			# validate
			queryValidate(validationWhere, None)
else:
	service.error("x3terna", "ErrorMsg", [StringUtils.format("Il tipo valore {0} per l'attributo {1} non e' supportato", attributeType, attributeName)])

