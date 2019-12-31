package vip.qsos.im.repository

import org.springframework.stereotype.Repository
import vip.qsos.im.lib.server.model.ImException
import vip.qsos.im.model.db.TableChatSessionOfGroup
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

    override fun create(name: String, memberList: List<String>): TableChatSessionOfGroup {
        /**较验账号是否授权*/
        memberList.forEach {
            if (false == mAccountRepository.findByAccount(it)?.used) {
                throw ImException("账号 $it 未授权")
            }
        }
        return mSessionOfGroupRepository.saveAndFlush(TableChatSessionOfGroup(
                name = name, member = TableChatSessionOfGroup.addMember(memberList)
        ))
    }

    override fun findByGroupId(groupId: Int): TableChatSessionOfGroup {
        return mSessionOfGroupRepository.findById(groupId).get()
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

    override fun updateMemberState(groupId: Int, member: String, online: Boolean) {
        val group = mSessionOfGroupRepository.findById(groupId).get()
        assert(member.length == 9)
        val memberStart = group.member.indexOf(member) - 1
        val newMember = (if (online) "1" else "0") + member
        group.member = group.member.replaceRange(memberStart, memberStart + 10, newMember)
        mSessionOfGroupRepository.save(group)
    }

    override fun leaveGroup(groupId: Int, member: String) {
        val group = mSessionOfGroupRepository.findById(groupId).get()
        assert(member.length == 9)
        val memberStart = group.member.indexOf(member) - 1
        group.member = group.member.replaceRange(memberStart, memberStart + 10, "")
        mSessionOfGroupRepository.save(group)
    }

    override fun deleteGroup(groupId: Int) {
        mSessionOfGroupRepository.deleteById(groupId)
    }
}