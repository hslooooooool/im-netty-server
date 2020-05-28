package vip.qsos.im.config

import org.springframework.context.annotation.Configuration
import vip.qsos.im.handler.HeartbeatHandler
import vip.qsos.im.handler.BindAccountRequestHandler
import vip.qsos.im.handler.SessionCloseRequestHandler
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