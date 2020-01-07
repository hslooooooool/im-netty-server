package vip.qsos.im.service

import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.ChatGroupBo
import vip.qsos.im.model.db.TableChatGroup
import vip.qsos.im.model.db.TableChatGroupOfLastRecord
import vip.qsos.im.model.db.TableChatSession
import vip.qsos.im.model.type.ChatType
import vip.qsos.im.repository.db.TableChatGroupOfLastRecordRepository
import vip.qsos.im.repository.db.TableChatGroupRepository
import vip.qsos.im.repository.db.TableChatSessionRepository
import javax.annotation.Resource

/**
 * @author : 华清松
 * 聊天群存储
 */
@Repository
class ChatGroupRepositoryImpl : ChatGroupRepository {
    @Resource
    private lateinit var mSessionRepository: TableChatSessionRepository
    @Resource
    private lateinit var mGroupRepository: TableChatGroupRepository
    @Resource
    private lateinit var mGroupOfLastRecordRepository: TableChatGroupOfLastRecordRepository
    @Resource
    private lateinit var mChatAccountRepository: ChatAccountRepository

    override fun findSingle(sender: String, receiver: String): ChatGroupBo? {
        var members = ""
        arrayListOf(sender, receiver).toSet().sorted().forEach {
            members += "0$it"
        }
        return mSessionRepository
                .findByChatTypeAndMember(ChatType.SINGLE, members)
                ?.let {
                    mGroupRepository.findBySessionId(it.sessionId)?.let { group ->
                        ChatGroupBo.getBo(it, group).addLastRecord(this.findGroupOfLastRecord(group.groupId))
                    }
                }
    }

    override fun create(name: String, creatorImAccount: String, memberList: List<String>): ChatGroupBo {
        val members = memberList.toSet().sorted()
        /**较验账号是否授权*/
        members.forEach {
            val joined = mChatAccountRepository.findByAccount(it)
            if (joined == null || !joined.used) {
                throw ImException("账号 $it 未授权")
            }
        }
        var chatType: ChatType = ChatType.SINGLE
        if (members.size > 2) {
            chatType = ChatType.GROUP
        }
        when {
            StringUtils.isEmpty(name) -> throw ImException("群名称不能为空")
            StringUtils.isEmpty(creatorImAccount) || creatorImAccount.length != 9 -> throw ImException("创建人不能为空")
        }
        val memberString = TableChatSession.addMember(members.toList())

        val group: TableChatGroup?
        var session = mSessionRepository.findByMember(memberString)
        if (session == null) {
            session = mSessionRepository.saveAndFlush(TableChatSession(
                    creator = creatorImAccount, member = memberString, chatType = chatType
            ))
            session!!
            group = mGroupRepository.saveAndFlush(TableChatGroup(
                    name = name, sessionId = session.sessionId
            ))
            group!!
        } else {
            group = mGroupRepository.findBySessionId(session.sessionId)
                    ?: throw ImException("消息群数据错误")
        }

        val record = mGroupOfLastRecordRepository.saveAndFlush(TableChatGroupOfLastRecord(groupId = group.groupId))
        return ChatGroupBo.getBo(session, group).addLastRecord(record)
    }

    override fun findGroup(sessionId: Long): ChatGroupBo {
        val session: TableChatSession
        try {
            session = mSessionRepository.findById(sessionId).get()
        } catch (e: Exception) {
            throw ImException("消息群不存在")
        }
        return mSessionRepository.findById(sessionId).get().let {
            findByGroupId(sessionId).let {
                var record = mGroupOfLastRecordRepository.findByGroupId(it.groupId)
                if (record == null) {
                    record = mGroupOfLastRecordRepository.saveAndFlush(TableChatGroupOfLastRecord(it.groupId))
                    record!!
                }
                ChatGroupBo.getBo(session, it).addLastRecord(record)
            }
        }
    }

    override fun findByGroupId(groupId: Long): TableChatGroup {
        val group: TableChatGroup
        try {
            group = mGroupRepository.findById(groupId).get()
        } catch (e: Exception) {
            throw ImException("聊天群不存在")
        }
        return group
    }

    override fun findByName(name: String, like: Boolean): List<TableChatGroup> {
        return if (like) {
            mGroupRepository.findByNameLike(name)
        } else {
            mGroupRepository.findByName(name)
        }
    }

    override fun list(): List<ChatGroupBo> {
        return mSessionRepository.findAll().map { session ->
            mGroupRepository.findBySessionId(session.sessionId)!!.let { group ->
                ChatGroupBo.getBo(session, group).addLastRecord(this.findGroupOfLastRecord(group.groupId))
            }
        }
    }

    override fun listLikeMember(member: String): List<ChatGroupBo> {
        return mSessionRepository.findByMemberLike(member).map { session ->
            mGroupRepository.findBySessionId(session.sessionId)!!.let { group ->
                ChatGroupBo.getBo(session, group).addLastRecord(this.findGroupOfLastRecord(group.groupId))
            }
        }
    }

    override fun joinGroup(groupId: Long, member: String) {
        val group: TableChatGroup = findByGroupId(groupId)
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
        val group: TableChatGroup = findByGroupId(groupId)
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
        val group: TableChatGroup = findByGroupId(groupId)
        mSessionRepository.deleteById(group.sessionId)
        mGroupRepository.deleteById(groupId)
    }

    override fun findGroupOfLastRecord(groupId: Long): TableChatGroupOfLastRecord {
        try {
            return mGroupOfLastRecordRepository.findById(groupId).get()
        } catch (e: Exception) {
            throw ImException("群信息缺失")
        }
    }
}