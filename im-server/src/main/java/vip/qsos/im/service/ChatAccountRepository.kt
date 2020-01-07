package vip.qsos.im.service

import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.db.TableChatAccount

/**
 * @author : 华清松
 * 聊天账号存储
 */
interface ChatAccountRepository {
    fun assign(): String
    fun findByAccount(account: String): TableChatAccount?
    fun list(used: Boolean?): List<TableChatAccount>
    @Throws(ImException::class)
    fun init(size: Int): List<TableChatAccount>
}