package vip.qsos.im.service

import org.springframework.stereotype.Service
import vip.qsos.im.lib.server.handler.IMServerInboundHandler
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.SessionClientBo
import vip.qsos.im.model.db.TableSessionClient
import vip.qsos.im.repository.db.TableSessionClientRepository
import javax.annotation.Resource

@Service
class SessionClientServiceImpl(
        @Resource private val mImServerInboundHandler: IMServerInboundHandler,
        @Resource private val mSessionClientRepository: TableSessionClientRepository
) : SessionClientService {

    override fun save(sessionClient: SessionClientBo) {
        if (sessionClient.nid != null) {
            val client = mSessionClientRepository
                    .findByNidOrAccount(sessionClient.nid!!, sessionClient.getAccount())
            if (client != null) {
                sessionClient.id = client.id
            }
        } else {
            throw ImException("请设置 ChannelId")
        }
        mSessionClientRepository.save(TableSessionClient.create(sessionClient))
    }

    override fun find(account: String): SessionClientBo? {
        val session = mSessionClientRepository.findByAccount(account)?.getBo()
        if (session != null) {
            session.channel = mImServerInboundHandler.getChannelByChannelId(session.nid)
        }
        return session
    }

    override fun remove(account: String) {
        mSessionClientRepository.deleteByAccount(account)
    }

    override fun list(): List<SessionClientBo> {
        return mSessionClientRepository.findAll().map {
            it.getBo()
        }
    }
}