package vip.qsos.im.repository

import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.Session
import java.util.concurrent.ConcurrentHashMap

/**
 * @author : 华清松
 * 正式场景下，使用redis或者数据库来存储
 */
@Repository
class SessionRepository : ISessionRepository {
    private val map = ConcurrentHashMap<String?, Session>()

    override fun save(session: Session) {
        session.getAccount()?.let {
            map[it] = session
        } ?: throw  NullPointerException("账号不能为空")
    }

    override fun find(account: String): Session? {
        return map[account]
    }

    override fun remove(account: String) {
        map.remove(account)
    }

    override fun list(): List<Session> {
        return map.values.toList()
    }
}