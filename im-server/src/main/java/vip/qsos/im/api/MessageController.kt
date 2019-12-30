package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.MessagePusher
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.form.SendMessageForm
import javax.annotation.Resource

@RestController
class MessageController : IMessageApi {
    @Resource
    private lateinit var messagePusher: MessagePusher

    override fun dispatch(message: SendMessageForm): BaseResult {
        return send(message)
    }

    override fun send(message: SendMessageForm): BaseResult {
        messagePusher.push(message.getMessage())
        return BaseResult.data()
    }
}