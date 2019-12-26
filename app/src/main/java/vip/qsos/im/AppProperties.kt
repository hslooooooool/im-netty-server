package vip.qsos.im

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties {
    @Value("\${im.server.host.ip}")
    lateinit var host: String
    @Value("\${im.server.host.name}")
    lateinit var hostName: String
    @Value("\${im.server.port}")
    var port: Int = 23456
    @Value("\${im.apple.apns.p12.password}")
    var applePassword: String? = null
    @Value("\${im.apple.apns.p12.file}")
    var appleFilePath: String? = null
    @Value("\${im.apple.apns.debug}")
    var appleDebug: Boolean = false
}