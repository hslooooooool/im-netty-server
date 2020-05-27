package vip.qsos.im.config

import org.springframework.context.annotation.Configuration
import vip.qsos.im.handler.BindAccountRequestHandler
import vip.qsos.im.handler.SessionCloseRequestHandler
import vip.qsos.im.lib.server.config.IMConstant
import vip.qsos.im.lib.server.handler.IMRequestHandler
import java.util.*

@Configuration
open class IMConfig : IMServerConfig() {

    override fun addHandler(handlerMap: HashMap<String, Class<out IMRequestHandler>>) {
        /**账号绑定handler*/
        handlerMap[IMConstant.CLIENT_BIND] = BindAccountRequestHandler::class.java
        /**连接关闭handler*/
        handlerMap[IMConstant.CLIENT_CLOSED] = SessionCloseRequestHandler::class.java
    }

}