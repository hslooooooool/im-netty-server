package com.farsunset.cim.service

import vip.qsos.im.lib.server.model.Session

/**
 * @author : 华清松
 * 消息 session 管理接口， 自行实现 AbstractSessionManager 接口来实现自己的 session 管理。
 */
interface IMSessionService {
    fun save(session: Session?)
    fun find(account: String?): Session?
    fun list(): List<Session?>?
    fun remove(account: String?)
}