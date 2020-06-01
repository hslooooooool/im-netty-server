package vip.qsos.im.data_jpa.handler

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.handler.IMSendBodyHandler
import vip.qsos.im.lib.server.model.IMSendBody
import vip.qsos.im.lib.server.model.IMSession

/**自定义消息请求处理
 * @author : 华清松
 */
@Component
class CustomMessageHandler : IMSendBodyHandler {
    override fun process(sessionClient: IMSession, body: IMSendBody) {
        println("❤️❤️❤️❤️$body\n❤️❤️❤️❤️")
    }
}