package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Component
import vip.qsos.im.data_jpa.repository.db.TableChatSessionRepository
import vip.qsos.im.dispense.MessageManager
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.AbsTableChatMessage
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.service.ChatMessageOfGroupService
import vip.qsos.im.service.ChatMessageOfSingleService
import javax.annotation.Resource

@Component
class MessageManagerImpl : MessageManager {
    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository

    @Resource
    private lateinit var mChatMessageOfSingleService: ChatMessageOfSingleService

    @Resource
    private lateinit var mChatMessageOfGroupService: ChatMessageOfGroupService

    override fun save(sessionId: Long, sessionType: EnumSessionType, message: Message): AbsTableChatMessage {
        return when (sessionType) {
            EnumSessionType.SINGLE -> {
                mChatMessageOfSingleService.save(sessionId, message)
            }
            EnumSessionType.GROUP -> {
                mChatMessageOfGroupService.save(sessionId, message)
            }
            else -> throw ImException("消息类型不支持")
        }
    }

    override fun find(sessionType: EnumSessionType, messageId: Long): Message? {
        return when (sessionType) {
            EnumSessionType.SINGLE -> {
                mChatMessageOfSingleService.find(messageId)?.getMessage()
            }
            EnumSessionType.GROUP -> {
                mChatMessageOfGroupService.find(messageId)?.getMessage()
            }
            else -> throw ImException("消息类型不支持")
        }
    }

    override fun remove(sessionType: EnumSessionType, messageId: Long) {
        when (sessionType) {
            EnumSessionType.SINGLE -> {
                mChatMessageOfSingleService.remove(messageId)
            }
            EnumSessionType.GROUP -> {
                mChatMessageOfGroupService.remove(messageId)
            }
            else -> throw ImException("消息类型不支持")
        }
    }

    override fun list(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<Message> {
        val session: TableChatSession
        try {
            session = mSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw ImException("会话 $sessionId 不存在")
        }
        session.let {
            return when (it.sessionType) {
                EnumSessionType.SINGLE -> {
                    mChatMessageOfSingleService.list(sessionId, timeline, size, previous).map { single ->
                        single.getMessage()
                    }
                }
                EnumSessionType.GROUP -> {
                    mChatMessageOfGroupService.list(sessionId, timeline, size, previous).map { group ->
                        group.getMessage()
                    }
                }
                else -> throw ImException("消息类型不支持")
            }
        }
    }

}