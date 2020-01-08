package vip.qsos.im.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.lib.server.model.SessionClient
import vip.qsos.im.model.db.TableSessionClient
import vip.qsos.im.repository.db.TableSessionClientRepository

/**
 * @author : 华清松
 * 会话存储
 */
@Repository
open class SessionClientRepositoryImpl @Autowired constructor(
        private val mSessionClientRepository: TableSessionClientRepository
) : SessionClientRepository {

    override fun save(sessionClient: SessionClient) {
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

    override fun find(account: String): SessionClient? {
        return mSessionClientRepository.findByAccount(account)?.getSession()
    }

    override fun remove(account: String) {
        mSessionClientRepository.deleteByAccount(account)
    }

    override fun list(): List<SessionClient> {
        return mSessionClientRepository.findAll().map {
            it.getSession()
        }
    }
}