package vip.qsos.im.api

import org.springframework.util.StringUtils
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
        return when {
            StringUtils.isEmpty(message.content) -> BaseResult.error("消息内容不能为空")
            StringUtils.isEmpty(message.sender) -> BaseResult.error("消息发送人不能为空")
            StringUtils.isEmpty(message.receiver) -> BaseResult.error("消息接收人不能为空")
            else -> {
                messagePusher.push(message)
                BaseResult.data()
            }
        }

    }
}