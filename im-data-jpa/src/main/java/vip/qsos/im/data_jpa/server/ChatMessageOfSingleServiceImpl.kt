package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Service
import vip.qsos.im.data_jpa.model.table.TableChatMessageOfSingle
import vip.qsos.im.data_jpa.repository.db.TableChatMessageOfSingleRepository
import vip.qsos.im.data_jpa.repository.db.TableChatSingleInfoRepository
import vip.qsos.im.data_jpa.repository.db.TableChatSingleRepository
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.lib.server.model.IMMessage
import javax.annotation.Resource

/**
 * @author : 华清松
 * 单聊消息存储
 */
@Service
open class ChatMessageOfSingleServiceImpl : ChatMessageOfSingleService {
    @Resource
    private lateinit var mSingleRepository: TableChatSingleRepository

    @Resource
    private lateinit var mSingleInfoRepository: TableChatSingleInfoRepository

    @Resource
    private lateinit var mMessageOfSingleRepository: TableChatMessageOfSingleRepository

    override fun save(sessionId: Long, message: IMMessage): IMMessage {
        val single = mSingleRepository.findBySessionId(sessionId)
                ?: throw IMException("会话不存在")
        // 获取会话信息
        val info = mSingleInfoRepository.findById(single.singleId).get()
        // 获取当前会话最后一条消息的时序,增加1并置入新消息中
        val timeline: Long = if (info.lastMessageId == null || info.lastMessageId!! <= 0L) {
            1L
        } else {
            val line = mMessageOfSingleRepository.findById(info.lastMessageId!!).get().timeline
            try {
                1L + line
            } catch (e: Exception) {
                throw IMException("会话 $sessionId 消息数已达上限 ${Long.MAX_VALUE} 条")
            }
        }
        // 保存新消息
        val msg = mMessageOfSingleRepository.saveAndFlush(TableChatMessageOfSingle.create(sessionId, message, timeline))
        // 更新会话信息最后一条消息ID
        info.lastMessageId = msg.messageId
        mSingleInfoRepository.save(info)
        return msg.getMessage()
    }

    override fun find(messageId: Long): IMMessage? {
        return try {
            mMessageOfSingleRepository.findById(messageId) .get()
        } catch (e: Exception) {
            null
        }
    }

    override fun remove(messageId: Long) {
        mMessageOfSingleRepository.deleteById(messageId)
    }

    override fun list(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<TableChatMessageOfSingle> {
        var startTimeline: Long
        val endTimeline: Long
        if (previous) {
            endTimeline = timeline
            startTimeline = endTimeline - size
            if (startTimeline < 0) {
                startTimeline = 1
            }
        } else {
            startTimeline = timeline
            endTimeline = try {
                startTimeline + size
            } catch (e: Exception) {
                Long.MAX_VALUE
            }
        }
        return mMessageOfSingleRepository.findBySessionIdAndTimelineBetween(sessionId, startTimeline, endTimeline)
    }

}