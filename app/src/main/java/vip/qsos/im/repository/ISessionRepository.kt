package vip.qsos.im.repository

import vip.qsos.im.lib.server.model.Session

/**
 * @author : 华清松
 * 消息 session 管理接口， 自行实现 ISessionManager 接口来实现自己的 session 管理。
 */
interface ISessionRepository {
    fun save(session: Session)
    fun find(account: String): Session?
    fun remove(account: String)
    fun list(): List<Session>
}