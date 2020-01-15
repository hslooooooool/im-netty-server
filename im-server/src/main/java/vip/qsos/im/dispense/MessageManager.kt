package vip.qsos.im.dispense

import vip.qsos.im.lib.server.model.Message
import vip.qsos.im.model.db.AbsTableChatMessage
import vip.qsos.im.model.type.EnumSessionType

interface MessageManager {
    fun save(sessionId: Long, sessionType: EnumSessionType, message: Message): AbsTableChatMessage
    fun find(sessionType: EnumSessionType, messageId: Long): Message?
    fun remove(sessionType: EnumSessionType, messageId: Long)
    fun list(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<Message>
}