package vip.qsos.im.service

import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.TableChatMessageOfGroup

/**
 * @author : 华清松
 */
interface ChatMessageOfGroupService {
    fun save(sessionId: Long, message: Message): TableChatMessageOfGroup
    fun find(messageId: Long): TableChatMessageOfGroup?
    fun remove(messageId: Long)
    fun list(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<TableChatMessageOfGroup>
}