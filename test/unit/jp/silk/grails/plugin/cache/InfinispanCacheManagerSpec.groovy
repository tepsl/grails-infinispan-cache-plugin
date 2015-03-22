package jp.silk.grails.plugin.cache

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.infinispan.client.hotrod.RemoteCacheManager
import spock.lang.Specification

/**
 * Created by tepsl on 15/03/19.
 */
@TestMixin(GrailsUnitTestMixin)
public class InfinispanCacheManagerSpec extends Specification {
	def doWithSpring = {
		ConfigObject infinispanConfig = new ConfigSlurper().parse('''\
infinispan.client.hotrod.server_list = '192.168.1.149:11222'
''')
		remoteCacheManager(RemoteCacheManager, infinispanConfig.toProperties())
		grailsCacheManager(GrailsInfinispanCacheManager, ref('remoteCacheManager'))
	}
	def setup() {
	}

	def cleanup() {
	}
}
