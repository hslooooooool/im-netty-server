package vip.qsos.im.api

import org.springframework.web.bind.annotation.RestController
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.BaseResult
import vip.qsos.im.model.ChatSession
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.repository.db.TableChatSessionRepository
import javax.annotation.Resource

@RestController
class SessionController : SessionApi {

    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository

    override fun findSingle(sender: String, receiver: String): BaseResult {
        val memberString = TableChatSession.addMember(arrayListOf(sender, receiver))
        val session = mSessionRepository
                .findBySessionTypeAndMember(EnumSessionType.SINGLE, memberString)
                ?: throw ImException("会话不存在")
        return BaseResult.data(session)
    }

    override fun findGroup(groupId: Long): BaseResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}