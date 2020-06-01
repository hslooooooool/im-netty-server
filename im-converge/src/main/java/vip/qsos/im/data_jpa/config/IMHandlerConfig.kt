package vip.qsos.im.data_jpa.config

import org.springframework.context.annotation.Configuration
import vip.qsos.im.config.AbsIMHandlerConfig
import vip.qsos.im.data_jpa.handler.BindAccountSendBodyHandler
import vip.qsos.im.data_jpa.handler.HeartbeatHandler
import vip.qsos.im.data_jpa.handler.SessionCloseSendBodyHandler
import vip.qsos.im.lib.server.handler.IMSendBodyHandler
import vip.qsos.im.lib.server.model.IMMessage
import vip.qsos.im.lib.server.model.IMSession
import java.util.*

@Configuration
open class IMHandlerConfig : AbsIMHandlerConfig() {

    override fun addHandler(handlerMap: HashMap<String, Class<out IMSendBodyHandler>>) {

    }

    override fun getClientClosedHandler(): Class<out IMSendBodyHandler>? {
        return SessionCloseSendBodyHandler::class.java
    }

    override fun getAccountBindHandler(): Class<out IMSendBodyHandler>? {
        return BindAccountSendBodyHandler::class.java
    }

    override fun getHeartbeatHandler(): Class<out IMSendBodyHandler>? {
        return HeartbeatHandler::class.java
    }

    override fun getClientActiveHandler(): Class<out IMSendBodyHandler>? {
        return null
    }

    override fun process(sessionClient: IMSession, message: IMMessage) {
        println("收到消息 $message")
    }

}