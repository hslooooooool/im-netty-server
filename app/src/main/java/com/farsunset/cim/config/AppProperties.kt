package com.farsunset.cim.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "im")
object AppProperties {
    @Value("\${im.server.host.ip}")
    lateinit var host: String
    @Value("\${im.server.port}")
    lateinit var port: String
}

@Component
@ConfigurationProperties(prefix = "apple")
object AppleProperties {
    @Value("\${apple.apns.p12.password}")
    var password: String? = null
    @Value("\${apple.apns.p12.file}")
    val filePath: String? = null
    @Value("\${apple.apns.debug}")
    var debug: Boolean = false
}