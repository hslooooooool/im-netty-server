package vip.qsos.im.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.Session
import vip.qsos.im.model.db.TableChatSessionOfClient
import vip.qsos.im.repository.db.IChatSessionRepository

/**
 * @author : 华清松
 * 会话存储
 */
@Repository
open class SessionRepository @Autowired constructor(
        private val mSessionRepository: IChatSessionRepository
) : ISessionRepository {

    override fun save(session: Session) {
        session.nid?.let {
            session.id = mSessionRepository.findByNid(it)?.getSession()?.id
        }
        session.id = session.id ?: find(session.getAccount())?.id
        mSessionRepository.save(TableChatSessionOfClient.create(session))
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