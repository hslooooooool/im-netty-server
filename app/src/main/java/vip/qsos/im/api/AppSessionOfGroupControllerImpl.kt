package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.dispense.MessageManager
import vip.qsos.im.dispense.MessagePusherImpl
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.db.AbsTableChatMessage
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.form.SendMessageInGroupForm
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.repository.db.TableChatSessionRepository
import javax.annotation.Resource

@RestController
class AppSessionOfGroupControllerImpl : AppSessionOfGroupApi {
    @Resource
    private lateinit var messagePusher: MessagePusherImpl
    @Resource
    private lateinit var mMessageManager: MessageManager
    @Resource
    private lateinit var mTableChatSessionRepository: TableChatSessionRepository

    override fun sendMessage(sessionId: Long, contentType: Int, content: String, sender: String): BaseResult {
        return this.send(SendMessageInGroupForm(sessionId, contentType, content, sender))
    }

    private fun send(message: SendMessageInGroupForm): BaseResult {
        /**验证会话*/
        val sessionId = message.sessionId
        val mTableChatSession: TableChatSession
        try {
            mTableChatSession = mTableChatSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw ImException("会话不存在")
        }

        /**识别会话类型*/
        val sMessage: AbsTableChatMessage?
        when (message.sessionType) {
            EnumSessionType.GROUP -> {
                mTableChatSession.getAccountList().map {
                    // 仅给未离群的账号发送消息
                    if (!it.leave && it.account != message.sender) {
                        try {
                            val msg = message.getMessage(it.account)
                            messagePusher.push(msg)
                        } catch (ignore: Exception) {
                            // 忽略未接收的消息用户
                        }
                    }
                }
                sMessage = mMessageManager.save(sessionId, message.sessionType, message.getMessage("${message.sessionId}"))
            }
            else -> {
                throw ImException("发送失败，此接口不支持此聊天类型的发送处理")
            }
        }
        return BaseResult.data(sMessage, "发送成功")
    }

}