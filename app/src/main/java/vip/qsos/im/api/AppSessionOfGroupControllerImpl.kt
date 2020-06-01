package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.data_jpa.repository.db.TableChatSessionRepository
import vip.qsos.im.data_jpa.server.IMMessagePusherImpl
import vip.qsos.im.service.IMMessageManager
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.data_jpa.model.table.TableChatSession
import vip.qsos.im.model.form.SendMessageInGroupForm
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.service.UserService
import javax.annotation.Resource

@RestController
class AppSessionOfGroupControllerImpl : AppSessionOfGroupApi {
    @Resource
    private lateinit var messagePusher: IMMessagePusherImpl

    @Resource
    private lateinit var mIMMessageManager: IMMessageManager

    @Resource
    private lateinit var mTableChatSessionRepository: TableChatSessionRepository

    @Resource
    private lateinit var userService: UserService

    override fun sendMessage(sessionId: Long, contentType: Int, content: String, sender: String): BaseResult {
        return this.send(SendMessageInGroupForm(sessionId, contentType, content, sender))
    }

    private fun send(form: SendMessageInGroupForm): BaseResult {
        /**验证会话*/
        val sessionId = form.sessionId
        val mTableChatSession: TableChatSession
        try {
            mTableChatSession = mTableChatSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw IMException("会话不存在")
        }

        /**识别会话类型*/
        if (EnumSessionType.GROUP == form.sessionType) {
            val senderUser = userService.findByImAccount(form.sender) ?: throw IMException("用户不存在")
            val msg = form.getMessage("${form.sessionId}", senderUser)
            val message = mIMMessageManager.save(
                    sessionId, form.sessionType, msg
            )
            msg.id = message.id
            mTableChatSession.getAccountList().map {
                // 仅给未离群的账号发送消息
                if (!it.leave && it.account != message.sender) {
                    try {
                        msg.receiver = it.account
                        messagePusher.push(msg)
                    } catch (ignore: Exception) {
                        // 忽略未接收的消息用户
                    }
                }
            }
            return BaseResult.data(message, "发送成功")
        } else {
            throw IMException("发送失败，此接口不支持此聊天类型的发送处理")

        }
    }

}