package vip.qsos.im.service

import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.TableChatMessageOfSingle
import vip.qsos.im.repository.db.TableChatMessageOfSingleRepository
import vip.qsos.im.repository.db.TableChatSingleInfoRepository
import vip.qsos.im.repository.db.TableChatSingleRepository
import javax.annotation.Resource

/**
 * @author : 华清松
 * 消息存储
 */
@Service
open class ChatMessageOfSingleServiceImpl : ChatMessageOfSingleService {
    @Resource
    private lateinit var mSingleRepository: TableChatSingleRepository
    @Resource
    private lateinit var mSingleInfoRepository: TableChatSingleInfoRepository
    @Resource
    private lateinit var mMessageOfSingleRepository: TableChatMessageOfSingleRepository

    override fun save(sessionId: Long, message: Message): TableChatMessageOfSingle {
        val single = mSingleRepository.findBySessionId(sessionId)
                ?: throw ImException("会话不存在")
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
                throw ImException("会话 $sessionId 消息数已达上限 ${Long.MAX_VALUE} 条")
            }
        }
        // 保存新消息
        val msg = mMessageOfSingleRepository.saveAndFlush(TableChatMessageOfSingle.create(sessionId, message, timeline))
        // 更新会话信息最后一条消息ID
        info.lastMessageId = msg.messageId
        mSingleInfoRepository.save(info)
        return msg
    }

    override fun find(messageId: Long): TableChatMessageOfSingle? {
        return try {
            mMessageOfSingleRepository.findById(messageId).get()
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