#---------------------------------------------------------------------------------------------------
#
# X4CACHESERVICE
#
#   1.0: implementation
#
#---------------------------------------------------------------------------------------------------


class CacheService:

	from psdi.util.logging import MXLoggerFactory
	from com.terna.common.string import StringUtils
	from com.terna.common.exception import MXLiteralException
	from java.util import HashMap
	from java.util import LinkedHashMap
	import sys 
	if '__pyclasspath__/Lib' not in sys.path:
		sys.path.append('__pyclasspath__/Lib')
	from threading import RLock	
	
	#
	# Constructor
	#
	def __init__(self, service):
		self.service = service
		self._initLogger()
		# the cache map for all the caches [Map<String, Map<String, Object>>]
		self.cacheMap = self.HashMap()
		# the cache hits map for all the caches [Map<String, Long>]
		self.keyLocks = self.HashMap()
		# lock
		self.lock = self.RLock()
		# max cache size
		self.maxSize = 5000

	class LRUCache(LinkedHashMap):
		from java.util import LinkedHashMap
		
		def __init__(self, maxSize):
			self.LinkedHashMap.__init__(self, maxSize, 0.75, True)
			self.maxSize = maxSize

		def removeEldestEntry(self, eldest):
			return self.size() > self.maxSize
	   
	class CacheEntry:
		def __init__(self, cacheService, name):
			self.name = name
			self.cacheService = cacheService
			self.entries = cacheService.LRUCache(self.cacheService.maxSize)
			self.hits = 0
			self.misses = 0
			self.gets = 0
			self.puts = 0
			
		def clear(self):
			self.entries.clear()
			self.hits = 0
			self.misses = 0
			self.gets = 0
			self.puts = 0
			
		# 
		#	Returns the cached value for the given key. 
		#	@param key[String]: key
		#	@return [Object]: cached value
		#
		def get(self, key):
			value = self.entries.get(key)
			if value != None:
				self.hits += 1
			else:
				self.misses += 1
			self.gets += 1
			return value
			
		# 
		#	Adds the value for the given key into the cache. 
		#	@param key[String]: key
		#	@param value[Object]: value
		#
		def put(self, key, value):
			self.entries.put(key, value)
			self.puts += 1
			
		# 
		#	Removes the value for the given key from the cache. 
		#	@param key[String]: key
		#
		def remove(self, key):
			self.entries.remove(key)
			
		# 
		#	Dumps cache statistics into the log
		#
		def stat(self):
			self.cacheService.info("{0}: size={1} hits={2} misses={3} gets={4} puts={5}", self.name, self.entries.size(), self.hits, self.misses, self.gets, self.puts)
			
		# 
		#	Dumps cache content into the log
		#
		def dump(self):
			self.stat()
			it = self.entries.keySet().iterator()
			while it.hasNext():
				key = it.next()
				self.cacheService.info("{0}: {1}", key, self.entries.get(key))

	# 
	# Set-up logger
	#
	def _initLogger(self):
		# set-up logger
		self.log = self.MXLoggerFactory.getLogger("maximo.catalog")
		
	# 
	# logs an INFO message 
	#
	def info(self, message, *args):
		# logs 
		self.log.info(self.StringUtils.format("[%s.%s] %s"%(self.service.scriptName, "X4CACHESERVICE", message), args))
		
	# 
	# logs an ERROR message 
	#
	def error(self, message, *args):
		# logs 
		self.log.error(self.StringUtils.format("[%s.%s] %s"%(self.service.scriptName, "X4CACHESERVICE", message), args))

	# 
	#	Sets the max size for the cache that will be added
	#	@param maxSize[int]: cache max size (must be greater than 0)
	#
	def setMaxSize(self, maxSize):
		if maxSize > 0:
			self.maxSize  =  maxSize
	# 
	#	Creates the cache with the given cache name	
	#	@param name[String]: cache name
	#
	def addCache(self, name):
		if self.StringUtils.isEmpty(name):
			# no cache name provided
			return
		if self.cacheMap.containsKey(name):
			# cache already in place
			return
		with self.lock:
			self.cacheMap.put(name, self.CacheEntry(self, name))
		
	# 
	#	Removes the cache with the given cache name	
	#	@param name[String]: cache name
	#
	def removeCache(self, name):
		with self.lock:
			self.cacheMap.remove(name)
		
	# 
	#	Clears the cache with the given cache name	
	#	@param name[String]: cache name
	#
	def clearCache(self, name):
		if self.cacheMap.containsKey(name):
			with self.lock:
				cache = self.cacheMap.get(name)
				cache.clear()

	# 
	#	Removes all the caches
	#
	def clear(self):
		with self.lock:
			self.cacheMap.clear()
	
	# 
	#	Returns the cached value for the given key. 
	#	@param name[String]: cache name
	#	@param key[String]: key
	#	@return [Object]: cached value
	#
	def get(self, name, key):
		if self.StringUtils.isEmpty(name):
			# no cache name provided
			return None
		if self.StringUtils.isEmpty(key):
			# no key provided
			return None
		with self.lock:
			cache = self.cacheMap.get(name)
			if cache == None:
				return None
			return cache.get(key)
		
	# 
	#	Adds the value for the given key in the required cache. 
	#	@param name[String]: cache name
	#	@param key[String]: key
	#	@param value[Object]: value
	#
	def put(self, name, key, value):
		if self.StringUtils.isEmpty(name):
			# no cache name provided
			return 
		if self.StringUtils.isEmpty(key):
			# no key provided
			return 
		with self.lock:
			cache = self.cacheMap.get(name)
			if cache == None:
				return
			# put the cache into the key
			cache.put(key, value)
			# remove the key lock
			self.removeKeyLock(name, key)
		
	# 
	#	Removes the value for the given key in the required cache. 
	#	@param name[String]: cache name
	#	@param key[String]: key
	#
	def remove(self, name, key):
		if self.StringUtils.isEmpty(name):
			# no cache name provided
			return 
		if self.StringUtils.isEmpty(key):
			# no key provided
			return 
		with self.lock:
			cache = self.cacheMap.get(name)
			if cache != None:
				cache.remove(key)

	# 
	#	Dumps cache statistics into the log
	#
	def stat(self):
		with self.lock:
			self.info("caches={0} key_locks={1}", self.cacheMap.size(), self.keyLocks.size())
			it = self.cacheMap.keySet().iterator()
			while it.hasNext():
				name = it.next()
				cache = self.cacheMap.get(name)
				cache.stat()
		
	# 
	#	Dumps cache content into the log
	#
	def dumpCache(self, name):
		with self.lock:
			cache = self.cacheMap.get(name)
			if cache != None:
				cache.dump()

	# 
	#	Dumps all cache content into the log
	#
	def dump(self):
		with self.lock:
			it = self.cacheMap.keySet().iterator()
			while it.hasNext():
				name = it.next()
				self.dumpCache(name)

	#
	#	Returns a lock for the given cache key
	#	@param name[String]: cache name
	#	@param key[String]: key
	#	@return the lock for the given cache key
	#
	def getKeyLock(self, name, key):
		with self.lock:
			k = name + "." + key
			if self.keyLocks.containsKey(k):
				return self.keyLocks.get(k)
			else:
				lock = self.RLock()
				self.keyLocks.put(k, lock)
				return lock
	
	#
	#	Removes the lock for the given cache key
	#	@param name[String]: cache name
	#	@param key[String]: key
	#	
	def removeKeyLock(self, name, key):
		with self.lock:
			k = name + "." + key
			self.keyLocks.remove(k)
