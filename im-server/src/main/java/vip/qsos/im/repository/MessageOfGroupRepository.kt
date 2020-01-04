package vip.qsos.im.repository

import vip.qsos.im.lib.server.model.Message

/**
 * @author : 华清松
 */
interface MessageOfGroupRepository {
    fun save(message: Message)
    fun find(messageId: Long): Message?
    fun remove(messageId: Long)
    fun list(): List<Message>
}