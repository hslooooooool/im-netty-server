package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import vip.qsos.im.data_jpa.repository.db.TableChatSessionRepository
import vip.qsos.im.data_jpa.repository.db.TableChatSingleInfoRepository
import vip.qsos.im.data_jpa.repository.db.TableChatSingleRepository
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.model.ChatSingleBo
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.db.TableChatSessionOfSingle
import vip.qsos.im.model.db.TableChatSessionOfSingleInfo
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.service.IMAccountService
import javax.annotation.Resource

@Service
class ChatSingleServiceImpl : ChatSingleService {
    @Resource
    private lateinit var mSingleRepository: TableChatSingleRepository

    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository

    @Resource
    private lateinit var mIMAccountService: IMAccountService

    @Resource
    private lateinit var mSingleInfoRepository: TableChatSingleInfoRepository

    override fun findById(id: Long): TableChatSessionOfSingle {
        try {
            return mSingleRepository.findById(id).get()
        } catch (e: Exception) {
            throw IMException("单聊不存在")
        }
    }

    override fun findBySessionId(sessionId: Long): TableChatSessionOfSingle? {
        return mSingleRepository.findBySessionId(sessionId)
    }

    override fun findSingle(sender: String, receiver: String): ChatSingleBo? {
        var members = ""
        arrayListOf(sender, receiver).toSet().sorted().forEach {
            members += "0$it"
        }
        return mSessionRepository.findBySessionTypeAndMember(EnumSessionType.SINGLE, members)?.let {
            mSingleRepository.findBySessionId(it.sessionId)?.let { group ->
                ChatSingleBo.getBo(it, group).setInfo(this.info(group.singleId))
            }
        }
    }

    override fun create(creator: String, receiver: String): ChatSingleBo {
        /**较验账号是否授权*/
        val joined = mIMAccountService.findByAccount(receiver)
        if (joined == null || !joined.used) {
            throw IMException("账号 $receiver 未授权")
        }
        when {
            StringUtils.isEmpty(creator) || creator.length != 9 -> throw IMException("创建人不能为空")
        }
        val memberString = TableChatSession.addMember(arrayListOf(creator, receiver).sorted())

        val single: TableChatSessionOfSingle?
        var session = mSessionRepository.findByMember(memberString)
        if (session == null) {
            session = mSessionRepository.saveAndFlush(TableChatSession(
                    creator = creator, member = memberString, sessionType = EnumSessionType.SINGLE
            ))
            session!!
            single = mSingleRepository.saveAndFlush(TableChatSessionOfSingle(
                    sessionId = session.sessionId
            ))
            single!!
        } else {
            single = mSingleRepository.findBySessionId(session.sessionId)
                    ?: throw IMException("消息群数据错误")
        }

        val info = mSingleInfoRepository.saveAndFlush(TableChatSessionOfSingleInfo(single.singleId))
        return ChatSingleBo.getBo(session, single).setInfo(info)
    }

    override fun find(sessionId: Long): ChatSingleBo {
        val session: TableChatSession
        try {
            session = mSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw IMException("消息群不存在")
        }
        return mSessionRepository.findById(sessionId).get().let {
            findBySingleId(sessionId).let {
                val info = mSingleInfoRepository.findById(it.singleId).get()
                ChatSingleBo.getBo(session, it).setInfo(info)
            }
        }
    }

    override fun findBySingleId(id: Long): TableChatSessionOfSingle {
        val group: TableChatSessionOfSingle
        try {
            group = mSingleRepository.findById(id).get()
        } catch (e: Exception) {
            throw IMException("单聊不存在")
        }
        return group
    }

    override fun list(): List<ChatSingleBo> {
        return mSessionRepository.findAll().map { session ->
            mSingleRepository.findBySessionId(session.sessionId)!!.let { group ->
                val sRecord = this.info(group.singleId)
                ChatSingleBo.getBo(session, group).setInfo(sRecord)
            }
        }
    }

    override fun listLikeMember(member: String): List<ChatSingleBo> {
        return mSessionRepository.findByMemberLike(member).map { session ->
            mSingleRepository.findBySessionId(session.sessionId)!!.let { group ->
                ChatSingleBo.getBo(session, group).setInfo(this.info(group.singleId))
            }
        }
    }

    override fun join(id: Long, member: String) {
        val group: TableChatSessionOfSingle = findBySingleId(id)
        val session = mSessionRepository.findById(group.sessionId).get()
        assert(member.length == 9)
        val joined = session.member.indexOf(member) > 0
        if (joined) {
            val memberStart = session.member.indexOf(member) - 1
            session.member = session.member.replaceRange(memberStart, memberStart + 1, "0")
        } else {
            session.member = session.member + "0$member"
        }
        mSessionRepository.saveAndFlush(session)
    }

    override fun leave(id: Long, member: String) {
        val group: TableChatSessionOfSingle = findBySingleId(id)
        val session = mSessionRepository.findById(group.sessionId).get()
        assert(member.length == 9)
        val memberStart = session.member.indexOf(member)
        val joined = memberStart > -1
        if (joined) {
            session.member = session.member.replaceRange(memberStart - 1, memberStart, "1")
            mSessionRepository.saveAndFlush(session)
        } else {
            throw IMException("此账号 $member 不在群内，无需移除")
        }
    }

    override fun delete(id: Long) {
        val group: TableChatSessionOfSingle = findBySingleId(id)
        mSessionRepository.deleteById(group.sessionId)
        mSingleRepository.deleteById(id)
    }

    override fun info(id: Long): TableChatSessionOfSingleInfo {
        try {
            return mSingleInfoRepository.findById(id).get()
        } catch (e: Exception) {
            throw IMException("群信息缺失")
        }
    }
}