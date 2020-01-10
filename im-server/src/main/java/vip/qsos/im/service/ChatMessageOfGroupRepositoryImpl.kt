package vip.qsos.im.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.TableChatMessageOfGroup
import vip.qsos.im.repository.db.TableChatGroupInfoRepository
import vip.qsos.im.repository.db.TableChatGroupRepository
import vip.qsos.im.repository.db.TableChatMessageOfGroupRepository


/**
 * @author : 华清松
 * 消息存储
 */
@Repository
open class ChatMessageOfGroupRepositoryImpl @Autowired constructor(
        private val mMessageRepository: TableChatMessageOfGroupRepository,
        private val mGroupRepository: TableChatGroupRepository,
        private val mGroupInfoRepository: TableChatGroupInfoRepository
) : ChatMessageOfGroupRepository {

    override fun save(sessionId: Long, message: Message): TableChatMessageOfGroup {
        val group = mGroupRepository.findBySessionId(sessionId)
                ?: throw ImException("聊天群不存在")
        val msg = mMessageRepository.saveAndFlush(TableChatMessageOfGroup.create(sessionId, message))
        val info = mGroupInfoRepository.findByGroupId(group.groupId)
        info.lastMessageId = msg.messageId
        mGroupInfoRepository.save(info)
        return msg
    }

    override fun find(messageId: Long): TableChatMessageOfGroup? {
        return try {
            mMessageRepository.findById(messageId).get()
        } catch (e: Exception) {
            null
        }
    }

    override fun remove(messageId: Long) {
        mMessageRepository.deleteById(messageId)
    }

    override fun list(): List<TableChatMessageOfGroup> {
        return mMessageRepository.findAll()
    }

}