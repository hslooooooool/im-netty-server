package vip.qsos.im.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.TableChatMessageOfSingle
import vip.qsos.im.repository.db.TableChatMessageOfSingleRepository
import vip.qsos.im.repository.db.TableChatSingleRepository

/**
 * @author : 华清松
 * 消息存储
 */
@Repository
open class ChatMessageOfSingleRepositoryImpl @Autowired constructor(
        private val mMessageRepository: TableChatMessageOfSingleRepository,
        private val mSingleRepository: TableChatSingleRepository,
        private val mSingleInfoRepository: TableChatSingleInfoRepository
) : ChatMessageOfSingleRepository {

    override fun save(sessionId: Long, message: Message): TableChatMessageOfSingle {
        val group = mSingleRepository.findBySessionId(sessionId)
                ?: throw ImException("会话不存在")
        val msg = mMessageRepository.saveAndFlush(TableChatMessageOfSingle.create(sessionId, message))
        val info = mSingleInfoRepository.findByGroupId(group.groupId)
        info.lastMessageId = msg.messageId
        mSingleInfoRepository.save(info)
        return msg
    }

    override fun find(messageId: Long): TableChatMessageOfSingle? {
        return try {
            mMessageRepository.findById(messageId).get()
        } catch (e: Exception) {
            null
        }
    }

    override fun remove(messageId: Long) {
        mMessageRepository.deleteById(messageId)
    }

    override fun list(): List<TableChatMessageOfSingle> {
        return mMessageRepository.findAll()
    }

}