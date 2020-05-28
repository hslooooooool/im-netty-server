package vip.qsos.im.data_jpa.server

import vip.qsos.im.model.ChatGroupBo
import vip.qsos.im.model.db.TableChatSessionOfGroup
import vip.qsos.im.model.db.TableChatSessionOfGroupInfo
import javax.transaction.Transactional

/**
 * @author : 华清松
 * 群聊服务接口
 */
@Transactional
interface ChatGroupService {
    fun findSingle(sender: String, receiver: String): ChatGroupBo?
    fun create(name: String, creatorImAccount: String, memberList: List<String> = arrayListOf()): ChatGroupBo
    fun findGroup(sessionId: Long): ChatGroupBo
    fun findByGroupId(groupId: Long): TableChatSessionOfGroup
    fun findByName(name: String, like: Boolean): List<TableChatSessionOfGroup>
    fun list(): List<ChatGroupBo>
    fun listLikeMember(member: String): List<ChatGroupBo>
    fun joinGroup(groupId: Long, member: String)
    fun leaveGroup(groupId: Long, member: String)
    fun deleteGroup(groupId: Long)
    fun findGroupInfo(groupId: Long): TableChatSessionOfGroupInfo
}