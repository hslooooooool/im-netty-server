package vip.qsos.im.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "im.server")
open class AppProperties {
    lateinit var hostIp: String
    lateinit var hostName: String
    var port: Int = 23456
    var apnsP12Password: String? = null
    var apnsP12File: String? = null
    var apnsDebug: Boolean = false
}