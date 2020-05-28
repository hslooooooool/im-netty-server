package vip.qsos.im.data_jpa.server

import vip.qsos.im.model.ChatSingleBo
import vip.qsos.im.model.db.TableChatSessionOfSingle
import vip.qsos.im.model.db.TableChatSessionOfSingleInfo
import vip.qsos.im.service.AbsService
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
    fun find(sessionId: Long): ChatSingleBo
    fun findBySingleId(id: Long): TableChatSessionOfSingle
    fun list(): List<ChatSingleBo>
    fun listLikeMember(member: String): List<ChatSingleBo>
    fun join(id: Long, member: String)
    fun leave(id: Long, member: String)
    fun delete(id: Long)
    fun info(id: Long): TableChatSessionOfSingleInfo
}