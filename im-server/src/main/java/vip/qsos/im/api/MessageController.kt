package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.MessagePusher
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.form.SendNoticeForm
import vip.qsos.im.model.form.SendTextMessageForm
import vip.qsos.im.model.type.ChatType
import vip.qsos.im.repository.GroupRepository
import vip.qsos.im.repository.MessageRepository
import javax.annotation.Resource

@RestController
class MessageController : MessageSendApi, MessageMangeApi {
    @Resource
    private lateinit var messagePusher: MessagePusher
    @Resource
    private lateinit var mMessageRepository: MessageRepository
    @Resource
    private lateinit var mGroupRepository: GroupRepository

    override fun send(action: String, contentType: Int, content: String, sender: String, groupId: String): BaseResult {
        return this.send(SendTextMessageForm(action, contentType, content, sender, groupId))
    }

    private fun send(message: SendTextMessageForm): BaseResult {
        var size = 0
        when (message.chatType) {
            ChatType.SINGLE, ChatType.GROUP -> {
                val groupId = message.groupId.toLong()
                mGroupRepository.findByGroupId(groupId).getAccountList().map {
                    /**给未离群的账号发送消息*/
                    if (!it.leave && it.account != message.sender) {
                        try {
                            size++
                            messagePusher.push(message.getMessage(it.account))
                        } catch (ignore: Exception) {
                            size--
                        }
                    }
                }
            }
            else -> {
                throw ImException("发送失败，此接口不支持此聊天类型的发送处理")
            }
        }
        return BaseResult.data("发送成功 $size 条")
    }

    override fun send(notice: SendNoticeForm): BaseResult {
        var size = 0
        notice.getMessageList().forEach { message ->
            try {
                size++
                messagePusher.push(message)
            } catch (ignore: Exception) {
                size--
            }
        }
        return BaseResult.data("发送成功 $size 条")
    }

    override fun list(): BaseResult {
        return BaseResult.data(mMessageRepository.list())
    }
}