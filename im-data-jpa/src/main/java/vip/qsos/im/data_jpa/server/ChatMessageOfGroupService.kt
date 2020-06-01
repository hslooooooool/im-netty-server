package vip.qsos.im.data_jpa.server

import vip.qsos.im.lib.server.model.IMMessage

interface ChatMessageOfGroupService {
    fun save(sessionId: Long, message: IMMessage): IMMessage
    fun find(messageId: Long): IMMessage?
    fun remove(messageId: Long)
    fun list(sessionId: Long, timeline: Long, size: Int, previous: Boolean): List<IMMessage>
}