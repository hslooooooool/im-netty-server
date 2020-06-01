package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Service
import vip.qsos.im.data_jpa.repository.TableSessionClientRepository
import vip.qsos.im.lib.server.handler.IMServerInboundHandler
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.lib.server.model.IMSession
import vip.qsos.im.model.db.TableIMSession
import vip.qsos.im.service.IMSessionService
import javax.annotation.Resource

@Service
class IMSessionServiceImpl : IMSessionService {
    @Resource
    private lateinit var mImServerInboundHandler: IMServerInboundHandler

    @Resource
    private lateinit var mSessionClientRepository: TableSessionClientRepository

    override fun save(sessionClient: IMSession) {
        if (sessionClient.nid != null) {
            val client = mSessionClientRepository
                    .findByNidOrAccount(sessionClient.nid!!, sessionClient.getAccount())
            if (client != null) {
                sessionClient.id = client.id
            }
        } else {
            throw IMException("请设置 channel id")
        }
        mSessionClientRepository.save(TableIMSession.create(sessionClient))
    }

    override fun find(account: String): IMSession? {
        val session = mSessionClientRepository.findByAccount(account)?.getBo()
        if (session != null) {
            session.channel = mImServerInboundHandler.getChannelByChannelId(session.nid)
        }
        return session
    }

    override fun remove(account: String) {
        mSessionClientRepository.deleteByAccount(account)
    }

    override fun offline(account: String) {

    }

    override fun list(): List<IMSession> {
        return mSessionClientRepository.findAll().map {
            it.getBo()
        }
    }
}