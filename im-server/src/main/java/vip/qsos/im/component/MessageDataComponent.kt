package vip.qsos.im.component

import org.springframework.stereotype.Component
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.type.ChatType
import vip.qsos.im.repository.MessageOfGroupRepository
import vip.qsos.im.repository.MessageOfGroupRepositoryImpl
import javax.annotation.Resource

/**
 * @author : 华清松
 * 消息存储
 */
@Component
class MessageDataComponent {
    @Resource
    private lateinit var mMessageOfGroupRepository: MessageOfGroupRepository

    fun save(message: Message) {
        when (message.action) {
            ChatType.GROUP.name -> {
                mMessageOfGroupRepository.save(message)
            }
            else -> throw ImException("消息类型不支持")
        }
    }

    fun find(chatType: ChatType, messageId: Long): Message? {
        return when (chatType) {
            ChatType.GROUP -> {
                mMessageOfGroupRepository.find(messageId)
            }
            else -> throw ImException("消息类型不支持")
        }
    }

    fun remove(chatType: ChatType, messageId: Long) {
        when (chatType) {
            ChatType.GROUP -> {
                mMessageOfGroupRepository.remove(messageId)
            }
            else -> throw ImException("消息类型不支持")
        }
    }

    fun list(chatType: ChatType): List<Message> {
        return when (chatType) {
            ChatType.GROUP -> {
                mMessageOfGroupRepository.list()
            }
            else -> throw ImException("消息类型不支持")
        }
    }

}