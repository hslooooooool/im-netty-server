package vip.qsos.im.data_jpa.server

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import vip.qsos.im.data_jpa.repository.db.TableChatGroupInfoRepository
import vip.qsos.im.data_jpa.repository.db.TableChatGroupRepository
import vip.qsos.im.data_jpa.repository.db.TableChatSessionRepository
import vip.qsos.im.lib.server.model.IMException
import vip.qsos.im.model.ChatGroupBo
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.db.TableChatSessionOfGroup
import vip.qsos.im.model.db.TableChatSessionOfGroupInfo
import vip.qsos.im.model.type.EnumSessionType
import vip.qsos.im.service.IMAccountService
import javax.annotation.Resource

/**
 * @author : 华清松
 * 聊天群存储
 */
@Service
class ChatGroupServiceImpl : ChatGroupService {
    @Resource
    private lateinit var mGroupRepository: TableChatGroupRepository

    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository

    @Resource
    private lateinit var mIMAccountService: IMAccountService

    @Resource
    private lateinit var mGroupInfoRepository: TableChatGroupInfoRepository

    override fun findSingle(sender: String, receiver: String): ChatGroupBo? {
        var members = ""
        arrayListOf(sender, receiver).toSet().sorted().forEach {
            members += "0$it"
        }
        return mSessionRepository
                .findBySessionTypeAndMember(EnumSessionType.SINGLE, members)?.let {
                    mGroupRepository.findBySessionId(it.sessionId)?.let { group ->
                        ChatGroupBo.getBo(it, group).setInfo(this.findGroupInfo(group.groupId))
                    }
                }
    }

    override fun create(name: String, creatorImAccount: String, memberList: List<String>): ChatGroupBo {
        val members = memberList.toSet().sorted()
        /**较验账号是否授权*/
        members.forEach {
            val joined = mIMAccountService.findByAccount(it)
            if (joined == null || !joined.used) {
                throw IMException("账号 $it 未授权")
            }
        }
        var sessionType: EnumSessionType = EnumSessionType.SINGLE
        if (members.size > 2) {
            sessionType = EnumSessionType.GROUP
        }
        when {
            StringUtils.isEmpty(name) -> throw IMException("群名称不能为空")
            StringUtils.isEmpty(creatorImAccount) || creatorImAccount.length != 9 -> throw IMException("创建人不能为空")
        }
        val memberString = TableChatSession.addMember(members.toList())

        val group: TableChatSessionOfGroup?
        var session = mSessionRepository.findByMember(memberString)
        if (session == null) {
            session = mSessionRepository.saveAndFlush(TableChatSession(
                    creator = creatorImAccount, member = memberString, sessionType = sessionType
            ))
            session!!
            group = mGroupRepository.saveAndFlush(TableChatSessionOfGroup(
                    sessionId = session.sessionId, name = name
            ))
            group!!
        } else {
            group = mGroupRepository.findBySessionId(session.sessionId)
                    ?: throw IMException("消息群数据错误")
        }

        val info = mGroupInfoRepository.saveAndFlush(TableChatSessionOfGroupInfo(group.groupId))
        return ChatGroupBo.getBo(session, group).setInfo(info)
    }

    override fun findGroup(sessionId: Long): ChatGroupBo {
        val session: TableChatSession
        try {
            session = mSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw IMException("消息群不存在")
        }
        return mSessionRepository.findById(sessionId).get().let {
            findByGroupId(sessionId).let {
                val info = mGroupInfoRepository.findByGroupId(it.groupId)
                ChatGroupBo.getBo(session, it).setInfo(info)
            }
        }
    }

    override fun findByGroupId(groupId: Long): TableChatSessionOfGroup {
        val group: TableChatSessionOfGroup
        try {
            group = mGroupRepository.findById(groupId).get()
        } catch (e: Exception) {
            throw IMException("聊天群不存在")
        }
        return group
    }

    override fun findByName(name: String, like: Boolean): List<TableChatSessionOfGroup> {
        return if (like) {
            mGroupRepository.findByNameLike(name)
        } else {
            mGroupRepository.findByName(name)
        }
    }

    override fun list(): List<ChatGroupBo> {
        return mSessionRepository.findAll().map { session ->
            mGroupRepository.findBySessionId(session.sessionId)!!.let { group ->
                val sRecord = this.findGroupInfo(group.groupId)
                ChatGroupBo.getBo(session, group).setInfo(sRecord)
            }
        }
    }

    override fun listLikeMember(member: String): List<ChatGroupBo> {
        return mSessionRepository.findByMemberLike(member).map { session ->
            mGroupRepository.findBySessionId(session.sessionId)!!.let { group ->
                ChatGroupBo.getBo(session, group).setInfo(this.findGroupInfo(group.groupId))
            }
        }
    }

    override fun joinGroup(groupId: Long, member: String) {
        val group: TableChatSessionOfGroup = findByGroupId(groupId)
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
        val group: TableChatSessionOfGroup = findByGroupId(groupId)
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

    override fun deleteGroup(groupId: Long) {
        val group: TableChatSessionOfGroup = findByGroupId(groupId)
        mSessionRepository.deleteById(group.sessionId)
        mGroupRepository.deleteById(groupId)
    }

    override fun findGroupInfo(groupId: Long): TableChatSessionOfGroupInfo {
        try {
            return mGroupInfoRepository.findById(groupId).get()
        } catch (e: Exception) {
            throw IMException("群信息缺失")
        }
    }
}