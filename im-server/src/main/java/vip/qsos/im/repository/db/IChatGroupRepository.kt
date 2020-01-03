package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatGroup
import vip.qsos.im.model.type.ChatType

interface IChatGroupRepository : JpaRepository<TableChatGroup, Int> {

    fun findByMember(member: String): TableChatGroup?
    fun findByMemberLike(member: String): List<TableChatGroup>
    fun findByName(name: String): List<TableChatGroup>
    fun findByNameLike(name: String): List<TableChatGroup>
    fun findByChatTypeAndMember(chatType: ChatType, member: String): TableChatGroup?

}