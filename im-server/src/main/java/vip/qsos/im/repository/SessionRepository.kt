package vip.qsos.im.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.Session
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.repository.db.IChatSessionRepository


/**
 * @author : 华清松
 * 正式场景下，使用redis或者数据库来存储
 */
@Repository
open class SessionRepository @Autowired constructor(
        private val mSessionRepository: IChatSessionRepository
) : ISessionRepository {

    override fun save(session: Session) {
        session.getAccount()?.let {
            mSessionRepository.save(TableChatSession().create(session))
        } ?: throw  NullPointerException("账号不能为空")
    }

    override fun find(account: String): Session? {
        return mSessionRepository.findByAccount(account)?.getSession()
    }

    override fun remove(account: String) {
        mSessionRepository.deleteByAccount(account)
    }

    override fun list(): List<Session> {
        return mSessionRepository.findAll().map {
            it.getSession()
        }
    }
}