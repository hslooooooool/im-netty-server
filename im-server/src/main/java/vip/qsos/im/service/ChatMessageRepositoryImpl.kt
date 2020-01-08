package vip.qsos.im.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.TableChatMessage
import vip.qsos.im.repository.db.TableChatMessageRepository


/**
 * @author : 华清松
 * 消息存储
 */
@Repository
open class ChatMessageRepositoryImpl @Autowired constructor(
        private val mMessageRepository: TableChatMessageRepository
) : ChatMessageRepository {

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