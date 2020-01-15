package vip.qsos.im.service

import org.springframework.stereotype.Service
import vip.qsos.im.dispense.MessageManager
import vip.qsos.im.dispense.MessagePusherImpl
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.AbsTableChatMessage
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.form.SendMessageInSingleForm
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.repository.db.TableChatSessionRepository
import javax.annotation.Resource

@Service
class SessionOfSingleServiceImpl : SessionOfSingleService {
    @Resource
    private lateinit var messagePusher: MessagePusherImpl
    @Resource
    private lateinit var mMessageManager: MessageManager
    @Resource
    private lateinit var mTableChatSessionRepository: TableChatSessionRepository

    override fun sendMessage(sessionId: Long, contentType: Int, content: String, sender: String): AbsTableChatMessage {
        val form = SendMessageInSingleForm(sessionId, contentType, content, sender)
        /**验证会话*/
        val mTableChatSession: TableChatSession
        try {
            mTableChatSession = mTableChatSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw ImException("会话不存在")
        }

        /**识别会话类型*/
        if (EnumSessionType.SINGLE == form.sessionType) {
            mTableChatSession.getAccountList().map {
                // 仅给未离群的账号发送消息
                if (!it.leave && it.account != form.sender) {
                    try {
                        val msg = form.getMessage(it.account)
                        messagePusher.push(msg)
                    } catch (ignore: Exception) {
                        // 忽略未接收的消息用户,可以做消息缓存队列,延迟重新推送
                    }
                }
            }
            return mMessageManager.save(sessionId, form.sessionType, form.getMessage("${form.sessionId}"))
        } else {
            throw ImException("发送失败，此接口不支持此聊天类型的发送处理")
        }
    }

    override fun getMessageListByTimeline(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<Message> {
        return mMessageManager.list(sessionId, timeline, size, previous)
    }

}