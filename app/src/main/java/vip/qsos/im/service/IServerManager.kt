package vip.qsos.im.service

import vip.qsos.im.lib.server.model.Session

/**
 * @author : 华清松
 * 消息服务管理接口
 */
interface IServerManager {
    fun save(session: Session)
    fun find(account: String): Session?
    fun remove(account: String)
    fun list(): List<Session>
}