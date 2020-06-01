package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Component
import vip.qsos.im.data_jpa.repository.db.TableChatSessionRepository
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.lib.server.model.IMMessage
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.service.IMMessageManager
import javax.annotation.Resource

@Component
class IMMessageManagerImpl : IMMessageManager {
    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository

    @Resource
    private lateinit var mChatMessageOfSingleService: ChatMessageOfSingleService

    @Resource
    private lateinit var mChatMessageOfGroupService: ChatMessageOfGroupService

    override fun save(sessionId: Long, sessionType: EnumSessionType, message: IMMessage): IMMessage {
        return when (sessionType) {
            EnumSessionType.SINGLE -> {
                mChatMessageOfSingleService.save(sessionId, message)
            }
            EnumSessionType.GROUP -> {
                mChatMessageOfGroupService.save(sessionId, message)
            }
            else -> throw IMException("消息类型不支持")
        }
    }

    override fun find(sessionType: EnumSessionType, messageId: Long): IMMessage? {
        return when (sessionType) {
            EnumSessionType.SINGLE -> {
                mChatMessageOfSingleService.find(messageId)
            }
            EnumSessionType.GROUP -> {
                mChatMessageOfGroupService.find(messageId)
            }
            else -> throw IMException("消息类型不支持")
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
            else -> throw IMException("消息类型不支持")
        }
    }

    override fun list(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<IMMessage> {
        val session: TableChatSession
        try {
            session = mSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw IMException("会话 $sessionId 不存在")
        }
        session.let {
            return when (it.sessionType) {
                EnumSessionType.SINGLE -> {
                    mChatMessageOfSingleService.list(sessionId, timeline, size, previous)
                }
                EnumSessionType.GROUP -> {
                    mChatMessageOfGroupService.list(sessionId, timeline, size, previous)
                }
                else -> throw IMException("消息类型不支持")
            }
        }
    }

}