package vip.qsos.im.handler

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.handler.IMSendBodyHandler
import vip.qsos.im.lib.server.model.IMReplyBody
import vip.qsos.im.lib.server.model.IMSendBody
import vip.qsos.im.lib.server.model.IMSession

/**
 * @author : 华清松
 * 无处理器
 */
@Component
class IMNullHandler : IMSendBodyHandler {

    override fun process(sessionClient: IMSession, body: IMSendBody) {
        val reply = IMReplyBody()
        reply.key = body.key
        reply.code = "500"
        reply.timestamp = System.currentTimeMillis()
        reply.message = "无法处理[${reply.key}]消息类型"
        sessionClient.write(reply)
    }
}