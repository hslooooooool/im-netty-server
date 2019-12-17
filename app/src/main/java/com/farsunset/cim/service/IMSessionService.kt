package com.farsunset.cim.service

import vip.qsos.im.lib.server.model.IMSession

/**
 * @author : 华清松
 * 消息 session 管理接口， 自行实现 AbstractSessionManager 接口来实现自己的 session 管理。
 */
interface IMSessionService {
    fun save(session: IMSession?)
    fun find(account: String?): IMSession?
    fun list(): List<IMSession?>?
    fun remove(account: String?)
}