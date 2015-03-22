package jp.silk.grails.plugin.cache

import grails.plugin.cache.GrailsCacheManager
import groovy.transform.CompileStatic
import groovy.util.logging.Log4j
import org.infinispan.client.hotrod.RemoteCache
import org.infinispan.client.hotrod.RemoteCacheManager
import org.infinispan.spring.provider.SpringRemoteCacheManager
import org.springframework.cache.Cache

/**
 * Created by tepsl on 15/03/19.
 */
@Log4j
@CompileStatic
class GrailsInfinispanCacheManager extends SpringRemoteCacheManager implements GrailsCacheManager {
	final Map<String, String> cacheNameMapping
	GrailsInfinispanCacheManager(RemoteCacheManager nativeCacheManager, Map<String, String> cacheNameMapping) {
		super(nativeCacheManager)
		log.debug("mapping: ${cacheNameMapping}")
		this.cacheNameMapping = cacheNameMapping
	}

	@Override
	public Collection<String> getCacheNames() {
		cacheNameMapping.keySet()
	}

	@Override
	public Cache getCache(String name) {
		log.debug("mapping ${name} -> ${cacheNameMapping[name]}")
		super.getCache(cacheNameMapping[name])
	}

	@Override
	boolean cacheExists(String name) {
		return (nativeCacheManager.getCache(cacheNameMapping[name]) != null)
	}

	@Override
	boolean destroyCache(String name) {
		RemoteCache remoteCache = nativeCacheManager.getCache(cacheNameMapping[name])
		if (remoteCache != null) {
			remoteCache.stop()
		}
		return true
	}
}
