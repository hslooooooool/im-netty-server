package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.component.MessageDataComponent
import vip.qsos.im.component.MessagePusher
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.form.SendMessageInGroupForm
import vip.qsos.im.model.form.SendNoticeForm
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.repository.db.TableChatSessionRepository
import javax.annotation.Resource

@RestController
class MessageController : MessageSendApi, MessageMangeApi {
    @Resource
    private lateinit var messagePusher: MessagePusher
    @Resource
    private lateinit var mMessageDataComponent: MessageDataComponent
    @Resource
    private lateinit var mTableChatSessionRepository: TableChatSessionRepository

    override fun sendToGroup(sessionId: Long, contentType: Int, content: String, sender: String): BaseResult {
        return this.send(SendMessageInGroupForm(sessionId, contentType, content, sender))
    }

    private fun send(message: SendMessageInGroupForm): BaseResult {
        var size = 0
        when (message.sessionType) {
            EnumSessionType.SINGLE -> {

            }
            EnumSessionType.GROUP -> {
                val sessionId = message.sessionId
                mTableChatSessionRepository.findById(sessionId).get().getAccountList().map {
                    /**给未离群的账号发送消息*/
                    if (!it.leave && it.account != message.sender) {
                        try {
                            size++
                            val msg = message.getMessage(it.account)
                            messagePusher.push(msg)
                            mMessageDataComponent.save(sessionId, msg)
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

    override fun notice(notice: SendNoticeForm): BaseResult {
        var size = 0
        notice.getMessageList().forEach { msg ->
            try {
                size++
                messagePusher.push(msg)
            } catch (ignore: Exception) {
                size--
            }
        }
        return BaseResult.data("发送成功 $size 条")
    }

    override fun list(sessionType: EnumSessionType): BaseResult {
        return BaseResult.data(mMessageDataComponent.list(sessionType))
    }

}