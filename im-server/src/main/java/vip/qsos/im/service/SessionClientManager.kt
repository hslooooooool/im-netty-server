package vip.qsos.im.service

import vip.qsos.im.lib.server.model.SessionClient

/**
 * @author : 华清松
 * 消息服务管理接口
 */
interface SessionClientManager {
    fun save(sessionClient: SessionClient)
    fun find(account: String): SessionClient?
    fun remove(account: String)
    fun list(): List<SessionClient>
}