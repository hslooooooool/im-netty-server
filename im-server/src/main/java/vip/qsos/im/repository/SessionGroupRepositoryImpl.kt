package vip.qsos.im.repository

import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.ChatGroupBo
import vip.qsos.im.model.db.TableChatSessionOfGroup
import vip.qsos.im.model.type.ChatType
import vip.qsos.im.repository.db.IChatSessionOfGroupRepository
import javax.annotation.Resource

/**
 * @author : 华清松
 * 聊天群存储
 */
@Repository
class SessionGroupRepositoryImpl : ISessionGroupRepository {
    @Resource
    private lateinit var mSessionOfGroupRepository: IChatSessionOfGroupRepository
    @Resource
    private lateinit var mAccountRepository: IAccountRepository

    override fun create(name: String, memberList: List<String>): ChatGroupBo {
        val members = memberList.toSet()
        /**较验账号是否授权*/
        members.forEach {
            val joined = mAccountRepository.findByAccount(it)
            if (joined == null || !joined.used) {
                throw ImException("账号 $it 未授权")
            }
        }
        var chatType: ChatType = ChatType.SINGLE
        if (members.size > 2) {
            chatType = ChatType.GROUP
        }
        return mSessionOfGroupRepository.saveAndFlush(TableChatSessionOfGroup(
                name = name, member = TableChatSessionOfGroup.addMember(members.toList()),
                chatType = chatType
        )).let {
            ChatGroupBo.getBo(it)
        }
    }

    override fun findByGroupId(groupId: Int): TableChatSessionOfGroup {
        val group: TableChatSessionOfGroup
        try {
            group = mSessionOfGroupRepository.findById(groupId).get()
        } catch (e: Exception) {
            throw ImException("聊天群不存在")
        }
        return group
    }

    override fun findByName(name: String, like: Boolean): List<TableChatSessionOfGroup> {
        return if (like) {
            mSessionOfGroupRepository.findByNameLike(name)
        } else {
            mSessionOfGroupRepository.findByName(name)
        }
    }

    override fun list(): List<TableChatSessionOfGroup> {
        return mSessionOfGroupRepository.findAll()
    }

    override fun listLikeMember(member: String): List<TableChatSessionOfGroup> {
        return mSessionOfGroupRepository.findByMemberLike(member)
    }

    override fun joinGroup(groupId: Int, member: String) {
        val group: TableChatSessionOfGroup = findByGroupId(groupId)
        assert(member.length == 9)
        val joined = group.member.indexOf(member) > 0
        if (joined) {
            val memberStart = group.member.indexOf(member) - 1
            group.member = group.member.replaceRange(memberStart, memberStart + 1, "0")
        } else {
            group.member = group.member + "0$member"
        }
        mSessionOfGroupRepository.save(group)
    }

    override fun leaveGroup(groupId: Int, member: String) {
        val group: TableChatSessionOfGroup = findByGroupId(groupId)
        assert(member.length == 9)
        val memberStart = group.member.indexOf(member)
        val joined = memberStart > -1
        if (joined) {
            group.member = group.member.replaceRange(memberStart - 1, memberStart, "1")
            mSessionOfGroupRepository.save(group)
        } else {
            throw ImException("此账号 $member 不在群内，无需移除")
        }
    }

    override fun deleteGroup(groupId: Int) {
        mSessionOfGroupRepository.deleteById(groupId)
    }
}