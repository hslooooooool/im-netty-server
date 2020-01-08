package vip.qsos.im.service

import vip.qsos.im.model.ChatGroup
import vip.qsos.im.model.db.TableChatGroup
import vip.qsos.im.model.db.TableChatGroupInfo
import javax.transaction.Transactional

/**
 * @author : 华清松
 * 聊天群存储
 */
@Transactional
interface ChatGroupRepository {

    fun findSingle(sender: String, receiver: String): ChatGroup?
    fun create(name: String, creatorImAccount: String, memberList: List<String> = arrayListOf()): ChatGroup
    fun findGroup(sessionId: Long): ChatGroup
    fun findByGroupId(groupId: Long): TableChatGroup
    fun findByName(name: String, like: Boolean): List<TableChatGroup>
    fun list(): List<ChatGroup>
    fun listLikeMember(member: String): List<ChatGroup>
    fun joinGroup(groupId: Long, member: String)
    fun leaveGroup(groupId: Long, member: String)
    fun deleteGroup(groupId: Long)
    fun findGroupInfo(groupId: Long): TableChatGroupInfo

}