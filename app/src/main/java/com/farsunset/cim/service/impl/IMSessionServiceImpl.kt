package com.farsunset.cim.service.impl

import com.farsunset.cim.repository.SessionRepository
import com.farsunset.cim.service.IMSessionService
import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.handler.IMSocketAcceptor
import vip.qsos.im.lib.server.model.IMSession
import javax.annotation.Resource

@Service
class IMSessionServiceImpl : IMSessionService {
    @Resource
    private val nioSocketAcceptor: IMSocketAcceptor? = null
    @Resource
    private val sessionRepository: SessionRepository? = null

    override fun save(session: IMSession?) {
        sessionRepository!!.save(session!!)
    }

    override fun find(account: String?): IMSession? {
        val session = sessionRepository!![account]
        if (session != null) {
            session.session = nioSocketAcceptor!!.getManagedSession(session.nid)
        }
        return session
    }

    override fun remove(account: String?) {
        sessionRepository!!.remove(account)
    }

    override fun list(): List<IMSession?>? {
        return sessionRepository!!.findAll()
    }
}