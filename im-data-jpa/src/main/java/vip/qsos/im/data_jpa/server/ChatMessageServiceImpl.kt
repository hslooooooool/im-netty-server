package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Service
import vip.qsos.im.data_jpa.repository.db.TableChatMessageRepository
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.TableChatMessage
import vip.qsos.im.service.ChatMessageService
import javax.annotation.Resource

/**
 * @author : 华清松
 * 消息存储
 */
@Service
open class ChatMessageServiceImpl : ChatMessageService {
    @Resource
    private lateinit var mMessageRepository: TableChatMessageRepository

    override fun save(message: Message) {
        mMessageRepository.save(TableChatMessage.create(message))
    }

    override fun find(messageId: Long): Message? {
        return try {
            mMessageRepository.findById(messageId).get().getMessage()
        } catch (e: Exception) {
            null
        }
    }

    override fun remove(messageId: Long) {
        mMessageRepository.deleteById(messageId)
    }

    override fun list(): List<Message> {
        return mMessageRepository.findAll().map {
            it.getMessage()
        }
    }

}