package vip.qsos.im.handler

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.handler.IMRequestHandler
import vip.qsos.im.lib.server.model.ReplyBody
import vip.qsos.im.lib.server.model.SendBody
import vip.qsos.im.lib.server.model.SessionClientBo
import vip.qsos.im.service.SessionClientService
import javax.annotation.Resource

/**
 * @author : 华清松
 * 无处理器
 */
@Component
class NullHandler : IMRequestHandler {
    override fun process(sessionClient: SessionClientBo, message: SendBody) {
        val reply = ReplyBody()
        reply.key = message.key
        reply.code = "500"
        reply.timestamp = System.currentTimeMillis()
        reply.message = "无法处理[${reply.key}]消息类型"
        sessionClient.write(reply)
    }
}