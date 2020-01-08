package vip.qsos.im.service

import vip.qsos.im.lib.server.model.Message

/**
 * @author : 华清松
 */
interface ChatMessageRepository {
    fun save(message: Message)
    fun find(messageId: Long): Message?
    fun remove(messageId: Long)
    fun list(): List<Message>
}