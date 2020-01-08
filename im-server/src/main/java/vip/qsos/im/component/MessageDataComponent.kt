package vip.qsos.im.component

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.service.ChatMessageOfGroupRepository
import javax.annotation.Resource

/**
 * @author : 华清松
 * 消息存储
 */
@Component
class MessageDataComponent {
    @Resource
    private lateinit var mChatMessageOfGroupRepository: ChatMessageOfGroupRepository

    fun save(sessionId: Long, message: Message) {
        when (message.action) {
            EnumSessionType.GROUP.name -> {
                mChatMessageOfGroupRepository.save(sessionId, message)
            }
            else -> throw ImException("消息类型不支持")
        }
    }

    fun find(sessionType: EnumSessionType, messageId: Long): Message? {
        return when (sessionType) {
            EnumSessionType.GROUP -> {
                mChatMessageOfGroupRepository.find(messageId)?.getMessage()
            }
            else -> throw ImException("消息类型不支持")
        }
    }

    fun remove(sessionType: EnumSessionType, messageId: Long) {
        when (sessionType) {
            EnumSessionType.GROUP -> {
                mChatMessageOfGroupRepository.remove(messageId)
            }
            else -> throw ImException("消息类型不支持")
        }
    }

    fun list(sessionType: EnumSessionType): List<Message> {
        return when (sessionType) {
            EnumSessionType.GROUP -> {
                mChatMessageOfGroupRepository.list().map {
                    it.getMessage()
                }
            }
            else -> throw ImException("消息类型不支持")
        }
    }

}