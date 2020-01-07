package vip.qsos.im.service

import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.handler.IMServerInboundHandler
import vip.qsos.im.lib.server.model.SessionClient
import javax.annotation.Resource

@Service
class SessionClientManagerImpl(
        @Resource private val mImServerInboundHandler: IMServerInboundHandler,
        @Resource private val mSessionClientRepository: SessionClientRepository
) : SessionClientManager {

    override fun save(sessionClient: SessionClient) {
        mSessionClientRepository.save(sessionClient)
    }

    override fun find(account: String): SessionClient? {
        val session = mSessionClientRepository.find(account)
        if (session != null) {
            session.channel = mImServerInboundHandler.getChannelByChannelId(session.nid)
        }
        return session
    }

    override fun remove(account: String) {
        mSessionClientRepository.remove(account)
    }

    override fun list(): List<SessionClient> {
        return mSessionClientRepository.list()
    }
}