package vip.qsos.im.repository

import vip.qsos.im.model.ChatGroupBo
import vip.qsos.im.model.db.TableChatGroup
import vip.qsos.im.model.db.TableChatGroupOfLastRecord

/**
 * @author : 华清松
 * 聊天群存储
 */
interface GroupRepository {

    fun findSingle(sender: String, receiver: String): ChatGroupBo?
    fun create(name: String, creator: String, memberList: List<String> = arrayListOf()): ChatGroupBo
    fun findGroup(groupId: Long): ChatGroupBo
    fun findByGroupId(groupId: Long): TableChatGroup
    fun findByName(name: String, like: Boolean): List<TableChatGroup>
    fun list(): List<ChatGroupBo>
    fun listLikeMember(member: String): List<TableChatGroup>
    fun joinGroup(groupId: Long, member: String)
    fun leaveGroup(groupId: Long, member: String)
    fun deleteGroup(groupId: Long)
    fun findGroupOfLastRecord(groupId: Long): TableChatGroupOfLastRecord

}