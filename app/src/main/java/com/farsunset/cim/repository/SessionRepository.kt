package com.farsunset.cim.repository

import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.Session
import java.util.concurrent.ConcurrentHashMap

/**
 * @author : 华清松
 * 正式场景下，使用redis或者数据库来存储session信息
 */
@Repository
class SessionRepository {
    private val map = ConcurrentHashMap<String?, Session>()

    fun save(session: Session) {
        session.getAccount()?.let {
            map[it] = session
        } ?: throw  NullPointerException("会话ID不存在")
    }

    operator fun get(account: String?): Session? {
        return map[account]
    }

    fun remove(account: String?) {
        map.remove(account)
    }

    fun findAll(): List<Session> {
        return map.values.toList()
    }
}