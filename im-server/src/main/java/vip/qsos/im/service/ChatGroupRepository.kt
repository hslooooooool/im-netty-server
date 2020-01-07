package vip.qsos.im.service

import vip.qsos.im.model.ChatGroupBo
import vip.qsos.im.model.db.TableChatGroup
import vip.qsos.im.model.db.TableChatGroupOfLastRecord
import javax.transaction.Transactional

/**
 * @author : 华清松
 * 聊天群存储
 */
@Transactional
interface ChatGroupRepository {

    fun findSingle(sender: String, receiver: String): ChatGroupBo?
    fun create(name: String, creatorImAccount: String, memberList: List<String> = arrayListOf()): ChatGroupBo
    fun findGroup(sessionId: Long): ChatGroupBo
    fun findByGroupId(groupId: Long): TableChatGroup
    fun findByName(name: String, like: Boolean): List<TableChatGroup>
    fun list(): List<ChatGroupBo>
    fun listLikeMember(member: String): List<ChatGroupBo>
    fun joinGroup(groupId: Long, member: String)
    fun leaveGroup(groupId: Long, member: String)
    fun deleteGroup(groupId: Long)
    fun findGroupOfLastRecord(groupId: Long): TableChatGroupOfLastRecord

}