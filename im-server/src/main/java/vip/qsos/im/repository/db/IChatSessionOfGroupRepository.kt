package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatSessionOfGroup

interface IChatSessionOfGroupRepository : JpaRepository<TableChatSessionOfGroup, Int> {

    fun findByMemberLike(member: String): List<TableChatSessionOfGroup>
    fun findByName(name: String): List<TableChatSessionOfGroup>
    fun findByNameLike(name: String): List<TableChatSessionOfGroup>

}