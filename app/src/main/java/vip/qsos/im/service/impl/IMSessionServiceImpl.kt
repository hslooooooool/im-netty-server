package vip.qsos.im.service.impl

import vip.qsos.im.repository.SessionRepository
import vip.qsos.im.service.IMSessionService
import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.handler.IMSocketAcceptor
import vip.qsos.im.lib.server.model.Session
import javax.annotation.Resource

@Service
class IMSessionServiceImpl : IMSessionService {
    @Resource
    private val nioSocketAcceptor: IMSocketAcceptor? = null
    @Resource
    private val sessionRepository: SessionRepository? = null

    override fun save(session: Session?) {
        sessionRepository!!.save(session!!)
    }

    override fun find(account: String?): Session? {
        val session = sessionRepository!![account]
        if (session != null) {
            session.session = nioSocketAcceptor!!.getManagedSession(session.nid)
        }
        return session
    }

    override fun remove(account: String?) {
        sessionRepository!!.remove(account)
    }

    override fun list(): List<Session?>? {
        return sessionRepository!!.findAll()
    }
}