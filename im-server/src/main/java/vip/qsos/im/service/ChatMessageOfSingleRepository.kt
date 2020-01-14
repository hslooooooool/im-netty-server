package vip.qsos.im.service

import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.TableChatMessageOfSingle

/**
 * @author : 华清松
 */
interface ChatMessageOfSingleRepository {
    fun save(sessionId: Long, message: Message): TableChatMessageOfSingle
    fun find(messageId: Long): TableChatMessageOfSingle?
    fun remove(messageId: Long)
    fun list(): List<TableChatMessageOfSingle>
}