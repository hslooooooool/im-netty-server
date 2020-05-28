package vip.qsos.im.data_jpa.server

import vip.qsos.im.lib.server.model.IMMessage
import vip.qsos.im.data_jpa.model.table.TableChatMessageOfGroup

/**
 * @author : 华清松
 */
interface ChatMessageOfGroupService {
    fun save(sessionId: Long, message: IMMessage): IMMessage
    fun find(messageId: Long): TableChatMessageOfGroup?
    fun remove(messageId: Long)
    fun list(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<TableChatMessageOfGroup>
}