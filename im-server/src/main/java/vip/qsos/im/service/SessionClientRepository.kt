package vip.qsos.im.service

import vip.qsos.im.lib.server.model.SessionClient

/**
 * @author : 华清松
 * 聊天连接终端管理接口
 */
interface SessionClientRepository {
    fun save(sessionClient: SessionClient)
    fun find(account: String): SessionClient?
    fun remove(account: String)
    fun list(): List<SessionClient>
}