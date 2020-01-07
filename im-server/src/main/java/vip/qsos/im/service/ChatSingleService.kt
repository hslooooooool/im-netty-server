package vip.qsos.im.service

import vip.qsos.im.model.db.TableChatSingle

/**
 * @author : 华清松
 * 单聊服务接口
 */
interface ChatSingleService : AbsService<Long, TableChatSingle> {
    fun findBySessionId(sessionId: Long): TableChatSingle?
}