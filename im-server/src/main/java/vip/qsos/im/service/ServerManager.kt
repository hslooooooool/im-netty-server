package vip.qsos.im.service

import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.handler.IMServerInboundHandler
import vip.qsos.im.lib.server.model.Session
import vip.qsos.im.repository.SessionRepository
import javax.annotation.Resource

@Service
class ServerManager(
        @Resource private val mImServerInboundHandler: IMServerInboundHandler,
        @Resource private val mSessionRepository: SessionRepository
) : IServerManager {

    override fun save(session: Session) {
        mSessionRepository.save(session)
    }

    override fun find(account: String): Session? {
        val session = mSessionRepository.find(account)
        if (session != null) {
            session.channel = mImServerInboundHandler.getChannelByChannelId(session.nid)
        }
        return session
    }

    override fun remove(account: String) {
        mSessionRepository.remove(account)
    }

    override fun list(): List<Session> {
        return mSessionRepository.list()
    }
}