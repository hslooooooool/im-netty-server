package vip.qsos.im.service

import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.TableChatMessageOfGroup

/**
 * @author : 华清松
 */
interface ChatMessageOfGroupRepository {
    fun save(sessionId: Long, message: Message): TableChatMessageOfGroup
    fun find(messageId: Long): TableChatMessageOfGroup?
    fun remove(messageId: Long)
    fun list(): List<TableChatMessageOfGroup>
}