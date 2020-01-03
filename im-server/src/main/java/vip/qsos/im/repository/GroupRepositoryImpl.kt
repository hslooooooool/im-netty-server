package vip.qsos.im.repository

import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.ChatGroupBo
import vip.qsos.im.model.db.TableChatGroup
import vip.qsos.im.model.db.TableChatGroupOfLastRecord
import vip.qsos.im.model.type.ChatType
import vip.qsos.im.repository.db.IChatGroupOfLastRecordRepository
import vip.qsos.im.repository.db.IChatGroupRepository
import javax.annotation.Resource

/**
 * @author : 华清松
 * 聊天群存储
 */
@Repository
class GroupRepositoryImpl : GroupRepository {
    @Resource
    private lateinit var mGroupRepository: IChatGroupRepository
    @Resource
    private lateinit var mGroupOfLastRecordRepository: IChatGroupOfLastRecordRepository
    @Resource
    private lateinit var mAccountRepository: AccountRepository

    override fun findSingle(sender: String, receiver: String): ChatGroupBo? {
        var members = ""
        arrayListOf(sender, receiver).toSet().sorted().forEach {
            members = members + "0$it" + it
        }
        return mGroupRepository
                .findByChatTypeAndMember(ChatType.SINGLE, members)
                ?.let {
                    ChatGroupBo.getBo(it).addLastRecord(this.findGroupOfLastRecord(it.groupId!!))
                }
    }

    override fun create(name: String, creator: String, memberList: List<String>): ChatGroupBo {
        val members = memberList.toSet().sorted()
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
        when {
            StringUtils.isEmpty(name) -> throw ImException("群名称不能为空")
            StringUtils.isEmpty(creator) || creator.length != 9 -> throw ImException("创建人不能为空")
        }
        return mGroupRepository.saveAndFlush(TableChatGroup(
                name = name, creator = creator, member = TableChatGroup.addMember(members.toList()),
                chatType = chatType
        )).let {
            val record = mGroupOfLastRecordRepository.saveAndFlush(TableChatGroupOfLastRecord(groupId = it.groupId!!))
            ChatGroupBo.getBo(it).addLastRecord(record)
        }
    }

    override fun findByGroupId(groupId: Int): TableChatGroup {
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
        return mGroupRepository.findAll().map {
            ChatGroupBo.getBo(it).addLastRecord(this.findGroupOfLastRecord(it.groupId!!))
        }
    }

    override fun listLikeMember(member: String): List<TableChatGroup> {
        return mGroupRepository.findByMemberLike(member)
    }

    override fun joinGroup(groupId: Int, member: String) {
        val group: TableChatGroup = findByGroupId(groupId)
        assert(member.length == 9)
        val joined = group.member.indexOf(member) > 0
        if (joined) {
            val memberStart = group.member.indexOf(member) - 1
            group.member = group.member.replaceRange(memberStart, memberStart + 1, "0")
        } else {
            group.member = group.member + "0$member"
        }
        mGroupRepository.save(group)
    }

    override fun leaveGroup(groupId: Int, member: String) {
        val group: TableChatGroup = findByGroupId(groupId)
        assert(member.length == 9)
        val memberStart = group.member.indexOf(member)
        val joined = memberStart > -1
        if (joined) {
            group.member = group.member.replaceRange(memberStart - 1, memberStart, "1")
            mGroupRepository.save(group)
        } else {
            throw ImException("此账号 $member 不在群内，无需移除")
        }
    }

    override fun deleteGroup(groupId: Int) {
        mGroupRepository.deleteById(groupId)
    }

    override fun findGroupOfLastRecord(groupId: Int): TableChatGroupOfLastRecord {
        try {
            return mGroupOfLastRecordRepository.findById(groupId).get()
        } catch (e: Exception) {
            throw ImException("群信息缺失")
        }
    }
}