package vip.qsos.im.service

import vip.qsos.im.lib.server.model.SessionClientBo

/**
 * @author : 华清松
 * 消息服务管理接口
 */
interface SessionClientService {
    fun save(sessionClient: SessionClientBo)
    fun find(account: String): SessionClientBo?
    fun remove(account: String)
    fun list(): List<SessionClientBo>
}