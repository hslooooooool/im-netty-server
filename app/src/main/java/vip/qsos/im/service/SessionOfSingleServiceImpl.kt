package vip.qsos.im.service

import org.springframework.stereotype.Service
import vip.qsos.im.data_jpa.model.table.IMMessage
import vip.qsos.im.data_jpa.repository.db.TableChatSessionRepository
import vip.qsos.im.data_jpa.server.IMMessagePusherImpl
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.model.AppException
import vip.qsos.im.model.AppUserBo
import vip.qsos.im.model.ChatMessageBo
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.form.SendMessageInSingleForm
import vip.qsos.im.model.type.EnumSessionType
import javax.annotation.Resource

@Service
class SessionOfSingleServiceImpl : SessionOfSingleService {
    @Resource
    private lateinit var messagePusher: IMMessagePusherImpl

    @Resource
    private lateinit var mIMMessageManager: IMMessageManager

    @Resource
    private lateinit var mTableChatSessionRepository: TableChatSessionRepository

    @Resource
    private lateinit var userService: UserService

    override fun sendMessage(sessionId: Long, type: Int, data: String, sender: String): IMMessage {
        val form = SendMessageInSingleForm(sessionId, type, data, sender)

        /**验证会话*/
        val mTableChatSession: TableChatSession
        try {
            mTableChatSession = mTableChatSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw IMException("会话不存在")
        }

        /**识别会话类型*/
        if (EnumSessionType.SINGLE == form.sessionType) {
            val senderUser = userService.findByImAccount(sender) ?: throw IMException("用户不存在")
            val msg = form.getMessage("${form.sessionId}", senderUser)
            val message = mIMMessageManager.save(
                    sessionId, form.sessionType, msg
            )
            msg.id = message.id
            mTableChatSession.getAccountList().map {
                // 仅给未离群的账号发送消息
                if (!it.leave && it.account != form.sender) {
                    try {
                        msg.receiver = it.account
                        messagePusher.push(msg)
                    } catch (ignore: Exception) {
                        // 忽略未接收的消息用户,可以做消息缓存队列,延迟重新推送
                    }
                }
            }
            return message
        } else {
            throw IMException("发送失败，此接口不支持此聊天类型的发送处理")
        }
    }

    override fun getMessageListByTimeline(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<ChatMessageBo> {
        return mIMMessageManager.list(sessionId, timeline, size, previous).map {
            val user = userService.findByImAccount(it.sender) ?: throw AppException("用户不存在")
            ChatMessageBo(AppUserBo.getBo(user), it)
        }
    }

}