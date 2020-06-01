package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Service
import vip.qsos.im.data_jpa.model.table.TableChatMessageOfGroup
import vip.qsos.im.data_jpa.repository.db.TableChatGroupInfoRepository
import vip.qsos.im.data_jpa.repository.db.TableChatGroupRepository
import vip.qsos.im.data_jpa.repository.db.TableChatMessageOfGroupRepository
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.lib.server.model.IMMessage
import javax.annotation.Resource

/**
 * @author : 华清松
 * 群消息存储
 */
@Service
open class ChatMessageOfGroupServiceImpl : ChatMessageOfGroupService {
    @Resource
    private lateinit var mMessageRepository: TableChatMessageOfGroupRepository

    @Resource
    private lateinit var mGroupRepository: TableChatGroupRepository

    @Resource
    private lateinit var mGroupInfoRepository: TableChatGroupInfoRepository

    override fun save(sessionId: Long, message: IMMessage): IMMessage {
        val group = mGroupRepository.findBySessionId(sessionId)
                ?: throw IMException("会话不存在")
        val msg = mMessageRepository.saveAndFlush(TableChatMessageOfGroup.create(sessionId, message))
        val info = mGroupInfoRepository.findByGroupId(group.groupId)
        info.lastMessageId = msg.messageId
        mGroupInfoRepository.save(info)
        return msg.getMessage()
    }

    override fun find(messageId: Long): IMMessage? {
        return try {
            mMessageRepository.findById(messageId).get().getMessage()
        } catch (e: Exception) {
            null
        }
    }

    override fun remove(messageId: Long) {
        mMessageRepository.deleteById(messageId)
    }

    override fun list(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<IMMessage> {
        return mMessageRepository.findAll().map {
            it.getMessage()
        }.sortedBy { it.timestamp }
    }

}