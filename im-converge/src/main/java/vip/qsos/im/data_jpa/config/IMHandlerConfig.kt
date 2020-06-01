package vip.qsos.im.data_jpa.config

import org.springframework.context.annotation.Configuration
import vip.qsos.im.config.AbsIMHandlerConfig
import vip.qsos.im.data_jpa.handler.HeartbeatHandler
import vip.qsos.im.data_jpa.handler.BindAccountRequestHandler
import vip.qsos.im.data_jpa.handler.SessionCloseRequestHandler
import vip.qsos.im.lib.server.handler.IMRequestHandler
import java.util.*

@Configuration
open class IMHandlerConfig : AbsIMHandlerConfig() {

    override fun addHandler(handlerMap: HashMap<String, Class<out IMRequestHandler>>) {

    }

    override fun getClientClosedHandler(): Class<out IMRequestHandler>? {
        return SessionCloseRequestHandler::class.java
    }

    override fun getAccountBindHandler(): Class<out IMRequestHandler>? {
        return BindAccountRequestHandler::class.java
    }

    override fun getHeartbeatHandler(): Class<out IMRequestHandler>? {
        return HeartbeatHandler::class.java
    }

    override fun getClientActiveHandler(): Class<out IMRequestHandler>? {
        return null
    }
}