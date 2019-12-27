package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.MessagePusher
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.BaseResult
import javax.annotation.Resource

@RestController
class MessageController : IMessageApi {
    @Resource
    private lateinit var messagePusher: MessagePusher

    override fun dispatch(message: Message): BaseResult {
        return send(message)
    }

    override fun send(message: Message): BaseResult {
        messagePusher.push(message)
        return BaseResult.data()
    }
}