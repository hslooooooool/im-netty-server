package vip.qsos.im.service

import vip.qsos.im.model.ChatSingleBo
import vip.qsos.im.model.db.TableChatSessionOfSingle
import vip.qsos.im.model.db.TableChatSessionOfSingleInfo
import javax.transaction.Transactional

/**
 * @author : 华清松
 * 单聊服务接口
 */
@Transactional
interface ChatSingleService : AbsService<Long, TableChatSessionOfSingle> {
    fun findBySessionId(sessionId: Long): TableChatSessionOfSingle?
    fun findSingle(sender: String, receiver: String): ChatSingleBo?
    fun create( creator: String, receiver: String): ChatSingleBo
    fun findGroup(sessionId: Long): ChatSingleBo
    fun findByGroupId(groupId: Long): TableChatSessionOfSingle
    fun list(): List<ChatSingleBo>
    fun listLikeMember(member: String): List<ChatSingleBo>
    fun joinGroup(groupId: Long, member: String)
    fun leaveGroup(groupId: Long, member: String)
    fun deleteGroup(groupId: Long)
    fun findGroupInfo(groupId: Long): TableChatSessionOfSingleInfo
}