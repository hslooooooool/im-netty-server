package vip.qsos.im.data_jpa.handler

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.IMSendBody
import vip.qsos.im.lib.server.model.IMSession

/**心跳请求处理
 * @author : 华清松
 */
@Component
class HeartbeatHandler : IMRequestHandler {
    override fun process(sessionClient: IMSession, message: IMSendBody) {
        println("❤️❤️❤️❤️$message❤️❤️❤️❤️")
    }
}