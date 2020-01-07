package vip.qsos.im.service

import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.db.TableChatSingle
import vip.qsos.im.repository.db.TableChatSingleRepository
import javax.annotation.Resource

/**
 * @author : 华清松
 * 单聊服务接口
 */
@Service
class ChatSingleServiceImpl : ChatSingleService {
    @Resource
    private lateinit var mTableChatSingleRepository: TableChatSingleRepository

    override fun findById(id: Long): TableChatSingle {
        try {
            return mTableChatSingleRepository.findById(id).get()
        } catch (e: Exception) {
            throw ImException("单聊不存在")
        }
    }

    override fun findBySessionId(sessionId: Long): TableChatSingle? {
        return mTableChatSingleRepository.findBySessionId(sessionId)
    }
}