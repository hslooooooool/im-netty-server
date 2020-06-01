package vip.qsos.im.data_jpa.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.data_jpa.model.table.TableChatSessionOfGroup

@Repository
interface TableChatGroupRepository : JpaRepository<TableChatSessionOfGroup, Long> {

    fun findBySessionId(sessionId: Long): TableChatSessionOfGroup?
    fun findByName(name: String): List<TableChatSessionOfGroup>
    fun findByNameLike(name: String): List<TableChatSessionOfGroup>

}