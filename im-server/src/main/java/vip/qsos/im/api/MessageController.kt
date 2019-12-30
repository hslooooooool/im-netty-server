package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.MessagePusher
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.form.SendMessageForm
import vip.qsos.im.model.form.SendNoticeForm
import vip.qsos.im.model.type.ChatType
import javax.annotation.Resource

@RestController
class MessageController : IMessageApi {
    @Resource
    private lateinit var messagePusher: MessagePusher

    override fun dispatch(message: SendMessageForm): BaseResult {
        return send(message)
    }

    override fun dispatch(notice: SendNoticeForm): BaseResult {
        return send(notice)
    }

    override fun send(message: SendMessageForm): BaseResult {
        when (message.chatType) {
            ChatType.SINGLE -> {
                messagePusher.push(message.getMessage())
            }
            ChatType.GROUP -> {

            }
            else -> {
                throw ImException("发送失败，此接口不支持此聊天类型的发送处理")
            }
        }

        return BaseResult.data("发送成功")
    }

    override fun send(notice: SendNoticeForm): BaseResult {
        notice.getMessageList().forEach { message ->
            messagePusher.push(message)
        }
        return BaseResult.data("发送成功")
    }
}