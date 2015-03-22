import groovy.util.logging.Slf4j
import jp.silk.grails.plugin.cache.GrailsInfinispanCacheManager
import org.infinispan.client.hotrod.RemoteCacheManager

@Slf4j
class CacheInfinispanGrailsPlugin {
	def version = '0.1-SNAPSHOT'
	def grailsVersion = '2.4 > *'
	def loadAfter = ['cache']
	def pluginExcludes = [
		'grails-app/conf/*CacheConfig.groovy',
		'grails-app/views/error.gsp',
		'docs/**',
		'src/docs/**'
	]

	def title = 'Cache Infinispan Plugin'
	def author = 'tepsl'
	def authorEmail = 'tepsl@users.noreply.github.com'
	def description = 'A Infinispan-based implementation of the Cache Plugin'
	def documentation = 'http://grails.org/plugin/cache-infinispan'
	def license = 'APACHE'

	/*
	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional), this event occurs before
	}
	*/

	def doWithSpring = {
		ConfigObject cacheConfig = application.config.grails?.cache
		ConfigObject infinispanConfig = cacheConfig?.infinispan
		if (!infinispanConfig) {
			log.info "Infinispan Cache Plugin is disabled"
			return
		}
		if (infinispanConfig.remoteCache) {
			Properties remoteCacheConfiguration
			if (infinispanConfig.remoteCache.configuration != null) {
				remoteCacheConfiguration = infinispanConfig.remoteCache.configuration.toProperties()
			} else {
				remoteCacheConfiguration = new Properties()
			}
			log.info("Infinispan RemoteCache configuration: ${remoteCacheConfiguration}")
			remoteCacheManager(RemoteCacheManager, remoteCacheConfiguration)
			grailsCacheManager(GrailsInfinispanCacheManager, ref('remoteCacheManager'), infinispanConfig.remoteCache.nameMapping)
		}
	}

	/*
	def doWithDynamicMethods = { ctx ->
		// TODO Implement registering dynamic methods to classes (optional)
	}

	def doWithApplicationContext = { ctx ->
		// TODO Implement post initialization spring config (optional)
	}

	def onChange = { event ->
		// TODO Implement code that is executed when any artefact that this plugin is
		// watching is modified and reloaded. The event contains: event.source,
		// event.application, event.manager, event.ctx, and event.plugin.
	}

	def onConfigChange = { event ->
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}

	def onShutdown = { event ->
		// TODO Implement code that is executed when the application shuts down (optional)
	}
	*/
}
