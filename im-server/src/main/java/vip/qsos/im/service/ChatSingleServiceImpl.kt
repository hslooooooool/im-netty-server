package vip.qsos.im.service

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.ChatSingleBo
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.db.TableChatSessionOfSingle
import vip.qsos.im.model.db.TableChatSessionOfSingleInfo
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.repository.db.TableChatSessionRepository
import vip.qsos.im.repository.db.TableChatSingleInfoRepository
import vip.qsos.im.repository.db.TableChatSingleRepository
import javax.annotation.Resource

/**
 * @author : 华清松
 */
@Service
class ChatSingleServiceImpl : ChatSingleService {
    @Resource
    private lateinit var mSingleRepository: TableChatSingleRepository
    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository
    @Resource
    private lateinit var mChatAccountService: ChatAccountService
    @Resource
    private lateinit var mSingleInfoRepository: TableChatSingleInfoRepository

    override fun findById(id: Long): TableChatSessionOfSingle {
        try {
            return mSingleRepository.findById(id).get()
        } catch (e: Exception) {
            throw ImException("单聊不存在")
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
                ChatSingleBo.getBo(it, group).setInfo(this.findGroupInfo(group.singleId))
            }
        }
    }

    override fun create(creator: String, receiver: String): ChatSingleBo {
        /**较验账号是否授权*/
        val joined = mChatAccountService.findByAccount(receiver)
        if (joined == null || !joined.used) {
            throw ImException("账号 $receiver 未授权")
        }
        when {
            StringUtils.isEmpty(creator) || creator.length != 9 -> throw ImException("创建人不能为空")
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
                    ?: throw ImException("消息群数据错误")
        }

        val info = mSingleInfoRepository.saveAndFlush(TableChatSessionOfSingleInfo(single.singleId))
        return ChatSingleBo.getBo(session, single).setInfo(info)
    }

    override fun findGroup(sessionId: Long): ChatSingleBo {
        val session: TableChatSession
        try {
            session = mSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw ImException("消息群不存在")
        }
        return mSessionRepository.findById(sessionId).get().let {
            findByGroupId(sessionId).let {
                val info = mSingleInfoRepository.findById(it.singleId).get()
                ChatSingleBo.getBo(session, it).setInfo(info)
            }
        }
    }

    override fun findByGroupId(groupId: Long): TableChatSessionOfSingle {
        val group: TableChatSessionOfSingle
        try {
            group = mSingleRepository.findById(groupId).get()
        } catch (e: Exception) {
            throw ImException("单聊不存在")
        }
        return group
    }

    override fun list(): List<ChatSingleBo> {
        return mSessionRepository.findAll().map { session ->
            mSingleRepository.findBySessionId(session.sessionId)!!.let { group ->
                val sRecord = this.findGroupInfo(group.singleId)
                ChatSingleBo.getBo(session, group).setInfo(sRecord)
            }
        }
    }

    override fun listLikeMember(member: String): List<ChatSingleBo> {
        return mSessionRepository.findByMemberLike(member).map { session ->
            mSingleRepository.findBySessionId(session.sessionId)!!.let { group ->
                ChatSingleBo.getBo(session, group).setInfo(this.findGroupInfo(group.singleId))
            }
        }
    }

    override fun joinGroup(groupId: Long, member: String) {
        val group: TableChatSessionOfSingle = findByGroupId(groupId)
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

    override fun leaveGroup(groupId: Long, member: String) {
        val group: TableChatSessionOfSingle = findByGroupId(groupId)
        val session = mSessionRepository.findById(group.sessionId).get()
        assert(member.length == 9)
        val memberStart = session.member.indexOf(member)
        val joined = memberStart > -1
        if (joined) {
            session.member = session.member.replaceRange(memberStart - 1, memberStart, "1")
            mSessionRepository.saveAndFlush(session)
        } else {
            throw ImException("此账号 $member 不在群内，无需移除")
        }
    }

    override fun deleteGroup(groupId: Long) {
        val group: TableChatSessionOfSingle = findByGroupId(groupId)
        mSessionRepository.deleteById(group.sessionId)
        mSingleRepository.deleteById(groupId)
    }

    override fun findGroupInfo(groupId: Long): TableChatSessionOfSingleInfo {
        try {
            return mSingleInfoRepository.findById(groupId).get()
        } catch (e: Exception) {
            throw ImException("群信息缺失")
        }
    }
}