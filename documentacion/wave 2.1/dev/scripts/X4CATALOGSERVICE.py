#---------------------------------------------------------------------------------------------------
#
# X4CATALOGSERVICE
#
#   1.0: implementation
#
#---------------------------------------------------------------------------------------------------


class CatalogService:

	from java.net import URL
	from com.ibm.json.java import JSONObject
	from java.io import BufferedReader
	from java.io import InputStreamReader
	from java.io import OutputStreamWriter
	from psdi.server import MXServer
	from psdi.util import MXFormat
	from psdi.util.logging import MXLoggerFactory
	from psdi.mbo import SqlFormat
	from psdi.mbo import MboConstants
	from com.terna.common.string import StringUtils
	from com.terna.common.db import DbUtils
	from com.terna.integration.common.asset import AssetKey
	from com.terna.common.maximo import MXUtils
	from com.terna.common.exception import MXLiteralException
	from java.util import Set
	from java.util import HashSet
	from java.util import ArrayList
	from java.util import HashMap
	from java.lang import StringBuffer
	from java.text import SimpleDateFormat
	
	#
	# Constructor
	#
	def __init__(self, service, cacheService = None, infoLog = True):
		self.service = service
		# store the cache service
		self.cacheService = cacheService
		if self.cacheService == None:
			# create a local catalog cache service
			self.cacheService = service.invokeScript("X4CACHESERVICE").get("CacheService")(service)
		
		self.infoLog = infoLog
		self._initLogger()
		# catalog MBO (can be X4CATDIFFOR/X4CATINTERF) [MboRemote]
		self.catalogMbo = None
		# the catalog name [String]
		self.catalogName = None
		# the catalog specifications by asset types [Map<String, Map<String, List<String>>]
		self.catalogSpecifications = None
		# the catalog attributes by asset types [Map<String, Map<String, List<String>>]
		self.catalogAttributes = None
		# the catalog asset types [Map<String, MboRemote>]
		self.assetTypes = None
		# the X4CARATTRIBUTE attribute map: key is the external name shown to the user, value is the Maximo attribute name
		self.attributeMap = self._loadAttributeMap()
		# asset look-up URL
		self.assetLookUpUrl = self.MXServer.getMXServer().getProperty("x4.terna.assetlookup.url")
		# asset look-up connection time-out
		self.assetLookUpConnectionTimeOut = 10000
		# asset look-up read time-out
		self.assetLookUpReadTimeOut = 20000
		
	# 
	# Set-up logger
	#
	def _initLogger(self):
		# set-up logger
		self.log = self.MXLoggerFactory.getLogger("maximo.catalog")
		self.indentLevel = 0
		self.padding = ""
		self.logId = ""
	
	# 
	# Sets the log id (e.g. work order number)
	#
	def setLogId(self, logId):
		self.logId = "(" + logId + ")"
	
	# 
	# logs an INFO message 
	#
	def indent(self, value):
		self.indentLevel = self.indentLevel + value
		if self.indentLevel < 0:
			self.indentLevel = 0
		self.padding = " " * ( 4 * self.indentLevel )
	# 
	# logs an INFO message 
	#
	def info(self, message, *args):
		if self.infoLog:
			# logs 
			self.log.info(self.StringUtils.format("[%s.%s.%s]%s %s"%(self.service.scriptName, "X4CATALOGSERVICE", self.logId, self.padding, message), args))
		
	# 
	# logs an ERROR message 
	#
	def error(self, message, *args):
		# logs 
		self.log.error(self.StringUtils.format("[%s.%s.%s] %s"%(self.service.scriptName, "X4CATALOGSERVICE", self.logId, message), args))

	# 
	#	@param catalogMbo[MboRemote]: the catalog MBO (X4CATDIFFOR/X4CATINTERF)
	#	@param assetMbo[MboRemote]: the MBO for the asset to check
	# 	@return [boolean]: True if the catalog entry matches the asset
	#
	def checkCatalog(self, catalogMbo, assetMbo):
		# validate parameters
		if catalogMbo == None:
			# no catalog provided
			self.service.error("x3terna", "ErrorMsg", ["No catalog provided"])
		if assetMbo == None:
			# no asset provided
			self.service.error("x3terna", "ErrorMsg", ["No asset provided"])

		# set catalog name
		self.catalogName = catalogMbo.getName()
		# check catalog type (X4CATDIFFOR/X4CATINTERF)
		if not self.StringUtils.in(self.catalogName, "X4CATDIFFOR", "X4CATINTERF"):
			self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Unsupported catalog {0} ", self.catalogName)])

		# starting the check 
		self.info("checking catalog {0} for asset {1}", self._getCatalogId(catalogMbo), self.AssetKey(assetMbo))

		# get the asset key
		targetAssetKey = self.AssetKey(assetMbo)
		# store catalog MBO 
		self.catalogMbo = catalogMbo
		self.info("loading catalog configurations")
		# get the catalog specifications
		self.catalogSpecifications = self._loadAttributes("C")
		# get the catalog attributes
		self.catalogAttributes = self._loadAttributes("A")
		# get the catalog asset types
		self.assetTypes = self._loadChildrenAssetTypes()
		
		# validate asset classification
		if not self._validateAsset(targetAssetKey):
			self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Asset {0} classification does not match catalog classifications", targetAssetKey)])

		# evaluate target asset
		assets = self.ArrayList()
		assets.add(targetAssetKey)
		assetType = self.catalogMbo.getString("tipologiaasset")
		self.info("evaluating target asset {0} type {1}", targetAssetKey, assetType)
		evaluation = self._evalAssets(assetType, assets, "IDENTITA")
		if not evaluation:
			self.info("target asset {0} does not match the catalog", targetAssetKey)
			return False
			
		# check if the catalog is single element
		if self.catalogMbo.getBoolean("isoelemento"):
			self.info("catalog is single element. evalutation is {0}", evaluation)
			return evaluation
		
		# evaluate related assets
		self.info("evaluating related assets")
		# go thru asset types 
		it = self.assetTypes.keySet().iterator()
		while it.hasNext():
			# get the asset type
			assetType = it.next()
			# check the asset type is actually used by the catalog
			if ( not self.catalogSpecifications.keySet().contains(assetType) ) and (  not self.catalogAttributes.keySet().contains(assetType) ):
				self.info("asset types {0} not used in the catalog, skipping look-up", assetType)
				continue
			# get related assets for the asset type
			self.info("getting related assets for the asset type {0}", assetType)
			assets = self._assetLookUp(targetAssetKey, assetType)
			self.info("evaluating asset type {0}", assetType)
			# if no assets the evaluation will be negative
			if assets.size() == 0:
				self.info("no assets found")
				return False
			# evalutate assets 
			evaluation = self._evalAssets(assetType, assets)
			if not evaluation:
				self.info("assets for asset types {0} does not match the catalog", assetType)
				return False

		# all assets have passed the evaluation
		assets.clear()
		self.catalogMbo = None
		self.info("catalog has been matched")
		return True

	# 
	# Returns the rule for the given asset type
	#
	#	@param assetType[String]: the asset type
	# 	@return [String]: the rule to apply 
	#
	def _getRule(self, assetType):
		# get the result from the cache if present
		cacheKey = self.StringUtils.format("RULEID.{0}.{1}", self._getCatalogId(self.catalogMbo), assetType)
		value = self.cacheService.get("CATALOG", cacheKey)
		if value != None:
			return value
		
		# get a lock for the cache key
		with self.cacheService.getKeyLock("CATALOG", cacheKey):
			# check if the cache entry has been retrieved before getting the lock
			value = self.cacheService.get("CATALOG", cacheKey)
			if value != None:
				return value
		
			if not self.assetTypes.keySet().contains(assetType):
				self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Unexpected asset type {0}", assetType)])
			
			# get the asset type child MBO
			assetChildMbo = self.assetTypes.get(assetType)
			if assetChildMbo.isNull("ruleid") == None:
				self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Asset type {0} does not have the rule id set", assetType)])
			# get the rule id
			ruleId = assetChildMbo.getLong("ruleid")
			# get the quantification rule
			mboSet = self.MXUtils.getMboSet("X4_QUANTIFICATION_RULES")
			mboSet.setFlag(self.MboConstants.DISCARDABLE, True)
			where = self.SqlFormat("x4_quantification_rulesid in (select x4_quantification_rulesid from x4_rules where x4_rulesid = :0)")
			where.setLong(0, ruleId)
			mboSet.setWhere(where.format())
			qRule = mboSet.getMbo(0)
			mboSet.close()
			if qRule == None or qRule.isNull("rule"): 
				# no quantification rule found
				self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Rule not found for asset type {0} rule id {1}", assetType, ruleId)])
			# validate expected rules
			rule = qRule.getString("rule")
			if not self.StringUtils.in(rule, "ALMENO 1", "IDENTITA"):
				# no quantification rule found
				self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Unsupported rule {0} for asset type {1} rule id {2}", rule, assetType, ruleId)])
			
			# cache the rule
			self.cacheService.put("CATALOG", cacheKey, rule)
			# return the rule
			return rule
		
	# 
	# Evaluates the assets for the given asset type.
	#
	#	@param assetType[String]: the asset type
	#	@param assets[List<AssetKey>]: the asset key list
	#	@param rule[String]: the rule to use if provided, otherwise the rule will be retrieved
	#	@return True if the assets satisfies the rule
	#
	def _evalAssets(self, assetType, assets, rule = None):
		try:
			self.indent(1)
			self.info("evaluating {0} assets for asset type {1}", assets.size(), assetType)
			# get rule if not provided
			if rule == None:
				rule = self._getRule(assetType)
			self.info("using rule {0}", rule)

			# get the specification names for the asset type
			specNames = self._getCatalogSpecNames(assetType)
			# get the catalog specification values
			catalogSpecValues = self._getCatalogSpecValues(assetType)
			if specNames.size() > 0:
				# we've specification in the catalog for this asset type
				self.info("asset type {0} has the following {1} spec names {2}", assetType, specNames.size(), self._joinString(",", specNames))
			# get the attribute names for the asset type
			attrNames = self._getCatalogAttrNames(assetType)
			# get the catalog attribute values
			catalogAttrValues = self._getCatalogAttrValues(assetType)
			if attrNames.size() > 0:
				self.info("asset type {0} has the following {0} attribute names {2}", assetType, attrNames.size(), self._joinString(",", attrNames))

			if specNames.size() == 0 and attrNames.size() == 0 :
				# no specifications/attributes in the catalog, so the asset is matched
				self.info("no specifications/attributes in the catalog, so the evaluation is positive")
				return True

			# contains the evaluations for all the assets
			evaluations = self.ArrayList()
			for i in range(0, assets.size()):
				# add the evaluation
				evaluations.add(True)
				# get asset 
				assetKey = assets.get(i)
				self.info("[{0}] evaluating asset {1}", i, assetKey)	
				
				# evaluate the specification names for the asset 
				if specNames.size() > 0:
					self.info("evaluating asset specifications")
					assetSpecValues = self._getAssetSpecValues(assetKey, specNames)
					evaluation = self._evalAssetValues(assetKey, catalogSpecValues, assetSpecValues)
					evaluations.set(i, evaluations.get(i) and evaluation)
				
				# evaluate the attribute names for the asset 
				if attrNames.size() > 0:
					self.info("evaluating asset attributes")
					attrValues = self._getAssetAttrValues(assetKey, attrNames)
					evaluation = self._evalAssetValues(assetKey, catalogAttrValues, attrValues)
					evaluations.set(i, evaluations.get(i) and evaluation)
				
				self.info("[{0}] evaluation for asset {1} is {2}", i, assetKey, evaluations.get(i))
				
				# check if the rule is ready to be evaluated
				if self._isRuleEvalReady(rule, assets.size(), evaluations):
					# rule is ready to be evaluated
					self.info("rule {0} is ready to be evaluated, stop further asset evaluations", rule)
					break


			# evaluate the quantification rule
			self.info("apply quantification rule {0} for {1} evaluation(s)", rule, evaluations.size())				
			finalEvaluation = self._evalRule(rule, evaluations)
			self.info("final eveluation for quantification rule {0} is {1}", rule, finalEvaluation)
			return finalEvaluation
		finally:
			self.indent(-1)

	# 
	# Checks if the rule can be evaluated with the current collected evaluations.
	# If the rule is IDENTITA the rule can be evaluated if the number of assets to evaluate is 1 and the evaluations are 1.
	# If the rule is ALMENO 1 the rule can be evaluated if at least one evaluation is true.
	#
	#	@param rule[String]: rule name
	# 	@param assetNo[int]: number of assets being evaluated
	# 	@param evaluations[List<Boolean>]: current collected evaluations. Its size must grow up for each asset's evaluation.
	#	@return True if the rule is ready to be evaluated
	#
	def _isRuleEvalReady(self, rule, assetNo, evaluations):
		if rule == "IDENTITA":
			if evaluations.size() == 1 and assetNo == 1:
				return True
		elif rule == "ALMENO 1":
			for i in range(0, evaluations.size()):
				if evaluations.get(i):
					return True
		else:
			self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Unsupported rule {0}", rule)])
		
		return False
		
	# 
	# Checks the rule with the current collected evaluations.
	# If the rule is IDENTITA the rule is true if the number of the evaluations is 1 and the evaluation is true.
	# If the rule is ALMENO 1 if at least one evaluation is true.
	#
	#	@param rule[String]: rule name
	# 	@param evaluations[List<Boolean>]: collected evaluations
	#	@return True if the rule evaluates true
	#
	def _evalRule(self, rule, evaluations):
		# evaluate the rule
		finalEvaluation = False
		# we've evaluations, apply the quantification rule

		if rule == "IDENTITA":
			if evaluations.size() == 1 and evaluations.get(0) == True:
				finalEvaluation = True
		elif rule == "ALMENO 1":
			for i in range(0, evaluations.size()):
				if evaluations.get(i):
					finalEvaluation = True
					break
		else:
			self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Unsupported rule {0}", rule)])
			
		return finalEvaluation

	# 
	# Evaluates the catalog acceptable values (for both attributes and specifications) for the given asset.
	#
	#	@param assetKey[AssetKey]: the asset key
	# 	@param catalogValues [Map<String, List<String>>]: the map of catalog attributes/specifications and possible values
	# 	@param assetValues [Map<String, String>]: the map of asset attributes/specifications and values
	#	@return True if the asset attributes/specifications matches, False otherwise
	#
	def _evalAssetValues(self, assetKey, catalogValues, assetValues):
		try:
			self.indent(1)
			self.info("evaluating values for asset {0}", assetKey)
			it = catalogValues.keySet().iterator()
			while it.hasNext():
				name = it.next()
				self.info("evaluating {0}", name)
				# get accepted value from the cataog
				acceptedValues = catalogValues.get(name)
				# get the asset value
				assetValue = assetValues.get(name)
				self.info("asset value {0} catalog values {1}", assetValue, self._joinString(",", acceptedValues))
				if assetValue == None or not acceptedValues.contains(assetValue):
					# the asset value does not match the catalog
					self.info("the asset {0} does not match the catalog for {1}", assetKey, name)
					return False
			# all checks passed
			self.info("asset {0} matches the catalog", assetKey)
			return True
		finally:
			self.indent(-1)

	# 
	# Collects the specification values (ALN or NUM values) for the given asset.
	#
	#	@param assetKey[AssetKey]: the asset key
	#	@param specName[Set<String>]: the set of specification names
	# 	@return [Map<String,String>]: the map of specification values
	#
	def _getAssetSpecValues(self, assetKey, specNames):
		try:
			self.indent(1)
			self.info("loading specification values for asset {0}", assetKey)			

			# check if the result is in the cache
			cacheKey = self.StringUtils.format("ASSETSPEC.{0}.{1}", assetKey, specNames.toString())
			value = self.cacheService.get("ASSET", cacheKey)
			if value != None:
				return value

			# create output map
			outputMap = self.HashMap()
			
			# check required specifications
			if specNames.size() == 0:
				# no specifications required, skip
				self.info("no specifications required, skip")
				return outputMap
				
			# get the asset MBO
			asset = assetKey.getMbo()
			# get the asset specification set
			assetSpecSet = asset.getMboSet("ASSETSPEC")
			# go thru values
			self.info("specNames = {0}", specNames)
			assetSpec = assetSpecSet.moveFirst()
			while assetSpec != None:
				specName = assetSpec.getString("assetattrid")
				if specNames.contains(specName):
					# specification name matched
					# get the value
					value = "" 
					if not assetSpec.isNull("alnvalue"):
						value = assetSpec.getString("alnvalue")
					elif not assetSpec.isNull("numvalue"):
						value = assetSpec.getString("numvalue")
					# add to the output
					outputMap.put(specName, value)
				# move to next spec value
				assetSpec = assetSpecSet.moveNext()
			
			# free up DB resources
			assetSpecSet.close()
			# put into the cache
			self.cacheService.put("ASSET", cacheKey, outputMap)
			# return output
			return outputMap
		finally:
			self.indent(-1)
	
	# 
	# Collects the attribute values for the given asset.
	# The attribute names are different from the Maximo attribute names.
	# So before getting the value from the asset the attribute names must be remapped tp the Maximo attribute names
	# by using the map attributeMap.
	# The output map will still use as key the external name (used in the catalog).
	#
	#	@param assetKey[AssetKey]: the asset key
	#	@param attrNames[Set<String>]: the set of attribute names
	# 	@return [Map<String,String>]: the map of attribute values
	#
	def _getAssetAttrValues(self, assetKey, attrNames):
		try:
			self.indent(1)
			self.info("loading attribute values for asset {0}", assetKey)			
			# create output map
			outputMap = self.HashMap()
			
			# check required attributes
			if attrNames.size() == 0:
				# no attributes required, skip
				self.info("no attributes required, skip")
				return outputMap
				
			# get the asset MBO
			asset = assetKey.getMbo()
			# get attributes
			it = attrNames.iterator()
			while it.hasNext():
				attribute = it.next()
				attributeName = self.attributeMap.get(attribute)
				if self.StringUtils.isEmpty(attributeName):
					# no attribute name for the attribute
					raise self.MXLiteralException("asset attribute {0} is not mapped with a valid attribute name {0}", attributeName)
				# get attribute type
				attributeType = self.MXUtils.getAttributeType("ASSET", attributeName)
				# get string representation for the value based on the attribute type
				if attributeType == self.MXFormat.DATE:
					dateValue = asset.getDate(attributeName)
					dateFormat = self.SimpleDateFormat("dd/MM/yyyy")
					value = dateFormat.format(dateValue)
				else:
					value = asset.getString(attributeName)
				# add to the output
				outputMap.put(attribute, value)
				self.info("{0}({1})={2}", attribute, attributeName, value)	
				
			# return output
			return outputMap
		finally:
			self.indent(-1)
	
	# 
	# Checks if the asset has been classified with a classification compatible to the catalog.
	#
	#	@param asset[AssetKey]: the asset to validate
	# 	@return [boolean]: True if the asset matches the catalog asset type, False otherwise
	#
	def _validateAsset(self, asset):
		# get asset MBO
		asset = asset.getMbo()

		# get asset classification id
		if asset.isNull("classstructureid"):
			self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Asset {0} not classified", asset)])
		assetClassification = asset.getString("classstructure.classificationid")
		
		# get the asset type
		assetType = self.catalogMbo.getString("tipologiaasset")
		
		# get catalog classifications
		classificationRelation = None
		if self.catalogName == "X4CATDIFFOR":
			classificationRelation = "X4DIFFORLASSIFICATIONS"
		elif self.catalogName == "X4CATINTERF":
			classificationRelation = "X4INTERFCLASSIFICATIONS"
		classificationSet = self.catalogMbo.getMboSet(classificationRelation)

		# check the asset matches the catalog classifications		
		classification = classificationSet.moveFirst()
		matched = False
		while classification != None:
			if classification.getString("classificationid") == assetClassification:
				# classification found
				matched = True
				break
			# move to next classification
			classification = classificationSet.moveNext()

		# free up DB resources
		classificationSet.close()
		
		return matched

	# 
	#	@param assetType[String]: the asset type
	# 	@return [Set<String>]: the set of catalog specification names for the asset type
	#
	def _getCatalogSpecNames(self, assetType):
		if not self.catalogSpecifications.containsKey(assetType):
            # the asset type is not in the catalog specification, return an empty set
			return self.HashSet()
		
		return self.catalogSpecifications.get(assetType).keySet()
		
	# 
	#	@param assetType[String]: the asset type
	# 	@return [Map<String, List<String>>]: the map of catalog specification names/values for the asset type
	#
	def _getCatalogSpecValues(self, assetType):
		if not self.catalogSpecifications.containsKey(assetType):
            # the asset type is not in the catalog specification, return an empty map
			return self.HashMap()
		
		return self.catalogSpecifications.get(assetType)

	# 
	#	@param assetType[String]: the asset type
	# 	@return [Set]: the set of catalog attribute names for the asset type
	#
	def _getCatalogAttrNames(self, assetType):
		if not self.catalogAttributes.containsKey(assetType):
			# the asset type is not in the catalog specification, return an empty set
			return self.HashSet()

		return self.catalogAttributes.get(assetType).keySet()

	# 
	#	@param assetType[String]: the asset type
	# 	@return [Map<String, List<String>>]: the map of catalog specification names/values for the asset type
	#
	def _getCatalogAttrValues(self, assetType):
		if not self.catalogAttributes.containsKey(assetType):
            # the asset type is not in the catalog specification, return an empty map
			return self.HashMap()
		
		return self.catalogAttributes.get(assetType)

	# 
	#	Loads the attribute map from X4CARATTRIBUTE
	# 	@return [Map<String, String>]: the map of attribute: key is the external name shown to the user, value is the Maximo attribute name
	#
	def _loadAttributeMap(self):
		attributeMap = self.HashMap()
		mboSet = self.MXUtils.getMboSet("X4CARATTRIBUTE")
		mbo = mboSet.moveFirst()
		while mbo != None:
			attributeMap.put(mbo.getString("name"), mbo.getString("attributename"))
			mbo = mboSet.moveNext()
		return attributeMap

	# 
	#	Loads the attributes (X4CARDIFFOR/X4CARDINTERF) for the corresponding catalog (X4CATDIFFOR/X4CATINTERF).
	# 	The attribute will be filterd based in the required asset type (C/A)
	#	@param attributeType[String]: attribute type (C/A)
	# 	@return [Map<String, Map<String, List<String>>>]: the map of attributes for the catalog
	#
	def _loadAttributes(self, attributeType):
		try:
			self.indent(1)
			self.info("loading attribute for type {0}", attributeType)
			
			# get the result from the cache if present
			cacheKey = self.StringUtils.format("ATTRIBUTE.{0}.{1}", self._getCatalogId(self.catalogMbo), attributeType)
			value = self.cacheService.get("CATALOG", cacheKey)
			if value != None:
				return value
			# get a lock for the cache key
			with self.cacheService.getKeyLock("CATALOG", cacheKey):
				# check if the cache entry has been retrieved before getting the lock
				value = self.cacheService.get("CATALOG", cacheKey)
				if value != None:
					return value
			
				# create output map
				outputMap = self.HashMap()
				# get attribute set
				attributeSet = None
				if self.catalogName == "X4CATDIFFOR":
					attributeSet = self.catalogMbo.getMboSet("X4CARDIFFOR")
				elif self.catalogName == "X4CATINTERF":
					attributeSet = self.catalogMbo.getMboSet("X4CARDINTERF")
			
				# go thru catalog attributes 
				attribute = attributeSet.moveFirst()
				while attribute != None:
					if attribute.getString("attributetype") == attributeType:
						assetType = attribute.getString("tipologiaasset")
						attributeName = attribute.getString("attribute")
						attributeValue = attribute.getString("value")
						
						# get the attribute map 
						attributeMap = None
						if not outputMap.containsKey(assetType):
							self.info("adding asset type {0}", assetType)
							outputMap.put(assetType, self.HashMap())
						attributeMap = outputMap.get(assetType)
						
						# get the value list
						valueList = None
						if attributeMap.containsKey(attributeName):
							valueList = attributeMap.get(attributeName)
						else:
							valueList = self.ArrayList()
							self.info("adding attribute {0}", attributeName)
							attributeMap.put(attributeName, valueList)
						
						# add the value
						self.info("adding value {0} for type {1} attribute {2}", attributeValue, assetType, attributeName)
						valueList.add(attributeValue)
					
					# move on
					attribute = attributeSet.moveNext()
				
				# free up DB resources
				attributeSet.close()
				# put in the cache
				self.cacheService.put("CATALOG", cacheKey, outputMap)
				# return the map
				return outputMap
		finally:
			self.indent(-1)

	# 
	# Returns the asset types for the catalog entry.
	# The returned map can be empty.
	#
	# 	@return [Map<String, MboRemote>]: the map X4CATCHILD MBO for asset types
	#
	def _loadChildrenAssetTypes(self):
		try:
			self.indent(1)
			self.info("loading asset types")
			# get the result from the cache if present
			cacheKey = self.StringUtils.format("ASSETTYPES.{0}", self._getCatalogId(self.catalogMbo))
			value = self.cacheService.get("CATALOG", cacheKey)
			if value != None:
				return value
			# get a lock for the cache key
			with self.cacheService.getKeyLock("CATALOG", cacheKey):
				# check if the cache entry has been retrieved before getting the lock
				value = self.cacheService.get("CATALOG", cacheKey)
				if value != None:
					return value
					
				# create the output
				assetTypes = self.HashMap()
				# get related asset types
				catChildSet = self.catalogMbo.getMboSet("X4CATCHILD")
				# go thru related asset types
				catChild = catChildSet.moveFirst()
				while catChild != None:
					assetTypes.put(catChild.getString("tipologiatarget"), catChild)
					# move next
					catChild = catChildSet.moveNext()
				
				# free up DB resources
				catChildSet.close()
				
				# put in the cache
				self.cacheService.put("CATALOG", cacheKey, assetTypes)
				# return asset types
				self.info("found {0} asset types {1}", assetTypes.size(), self._joinString(",", assetTypes.keySet()))
				return assetTypes
		finally:
			self.indent(-1)

	
	# 
	# Invokes the asset look-up micro service to get the related assets for the given asset type.
	#
	#	@param targetAssetKey[String]: the root asset for the look-up
	#	@param assetType[String]: the asset type
	# 	@return [List<AssetKey>]: the list of asset keys for the given asset type
	#
	def _assetLookUp(self, targetAssetKey, assetType):
		# get the children asset type MBO
		catChild = self.assetTypes.get(assetType)
		# get asset type info
		ruleId = catChild.getLong("ruleid")
		classStructureId = catChild.getString("classstructureid")
		# add the asset type to the map
		assetList = self.ArrayList()
		# get asset from the look-up
		assetList.addAll(self.invokeAssetLookUp(targetAssetKey, ruleId, classStructureId))
		return assetList

	# 
	# Invokes the asset look-up micro service and returns a list of related assets.
	#
	#	@param targetAssetKey[AssetKey]: the root asset for the look-up
	#	@param ruleId[String]: rule id
	#	@param classStructureId[String]: class structure id
	# 	@return [List<AssetKey>]: the list of keys for the asset related by the rule id and class structure id
	#
	def invokeAssetLookUp(self, targetAssetKey, ruleId, classStructureId):
		try:
			self.info("looking-up assets for {0} rule {1} class {2}", targetAssetKey, ruleId, classStructureId)
			
			# check if the result is in the cache
			cacheKey = self.StringUtils.format("LOOKUP.{0}.{1}.{2}", targetAssetKey, ruleId, classStructureId)
			value = self.cacheService.get("ASSET", cacheKey)
			if value != None:
				return value
				
			with self.cacheService.getKeyLock("ASSET", cacheKey):
				value = self.cacheService.get("ASSET", cacheKey)
				if value != None:
					return value
				
				siteId = targetAssetKey.getSiteId()
				# prepare the request
				requestData = self.JSONObject()
				requestData.put("siteId", siteId)
				requestData.put("assetnum", targetAssetKey.getNum())
				requestData.put("ruleId", ruleId)
				if not self.StringUtils.isEmpty(classStructureId):
					requestData.put("classStructureIdTarget", classStructureId)
				jsonRequest = requestData.serialize()

				# open connection
				url = self.URL(self.assetLookUpUrl)
				self.info("invoking look-up assets at {0} request {1}", url, jsonRequest)
				connection = url.openConnection()
				connection.setRequestMethod("POST")
				connection.setConnectTimeout(self.assetLookUpConnectionTimeOut)
				connection.setReadTimeout(self.assetLookUpReadTimeOut)
				connection.setDoOutput(True)
				connection.setDoInput(True)
				connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
				connection.setRequestProperty("Accept", "application/json")

				# send request
				writer = self.OutputStreamWriter(connection.getOutputStream(), "UTF-8")
				writer.write(jsonRequest)
				writer.flush()
				writer.close()

				#  get the response
				status = connection.getResponseCode()
				success = 200 <= status < 300
				if success:
					stream = connection.getInputStream()
				else:
					stream = connection.getErrorStream()
				reader = self.BufferedReader(self.InputStreamReader(stream, "UTF-8"))
				response = ""
				line = reader.readLine()
				while line is not None:
					response += line
					line = reader.readLine()
				reader.close()
				
				# in case the call failed raise exception
				if not success:
					self.error("asset look-up returned response code {0}. response: {1}", status, response)
					raise self.MXLiteralException("asset look-up returned response code {0}. response: {1}", status, response)

				self.info("asset look-up returned response code {0}. response: {1}", status, response)
				# parse response
				jsonResponse = self.JSONObject.parse(response)
				if jsonResponse.get("esito") != "OK":
					self.error("asset look-up returned error {0}", jsonResponse.get("descrizione"))
					raise self.MXLiteralException("asset look-up returned error {0}", jsonResponse.get("descrizione"))

				# convert returned asset list
				assetList = jsonResponse.get("assets")
				assets = self.ArrayList()
				if assetList.size() > 0:
					# get the returned assets
					query = self.SqlFormat("siteid = :0 and assetnum in " + self.DbUtils.getInClause(assetList))
					query.setObject(0, "UPPER", siteId)
					where = query.format()
					# get asset MBO
					assetSet = self.MXUtils.getMboSet("ASSET")
					assetSet.setFlag(self.MboConstants.DISCARDABLE, True)
					assetSet.setWhere(where)
					assetMbo = assetSet.moveFirst()
					while assetMbo != None:
						assets.add(self.AssetKey(assetMbo))
						# move next
						assetMbo = assetSet.moveNext()
					
					assetSet.close()

				self.info("got {0} assets: {1}", assets.size(), self.StringUtils.join(", ", assets))
				# validate the number of assets found versus the look-up assets
				if assetList.size() != assets.size():
					self.error("asset look-up returned {0} assets found {1} assets", assetList.size(), assets.size())
					raise self.MXLiteralException("asset look-up returned error {0}", assetList.size(), assets.size())

				# put in the cache
				self.cacheService.put("ASSET", cacheKey, assets)
				# return assets
				return assets
		finally:
			pass
			
	#
	# @return [String]: the catalog id (iddifformita for X4CATDIFFOR and idinterferenza for X4CATINTERF)
	#
	def _getCatalogId(self, catalogMbo):
		name = catalogMbo.getName()
		if name == "X4CATDIFFOR":
			return catalogMbo.getString("iddifformita")
		elif name == "X4CATINTERF":
			return catalogMbo.getString("idinterferenza")
		else:
			self.service.error("x3terna", "ErrorMsg", [self.StringUtils.format("Unsupported catalog {0}", name)])

	# 
	# Joins all the strings using the given separator. The separator will not be added at the end.
	#
	#	@param sep[String]: separator
	#	@param collection[Collection<String>]: collection of strings
	# 	@return [String]: the joined string
	#
	def _joinString(self, sep, collection):
		r = self.StringBuffer()
		first = True
		it = collection.iterator()
		while it.hasNext():
			string = it.next()
			if not first:
				r.append(sep)
			r.append(string)
			first = False
		return r.toString()