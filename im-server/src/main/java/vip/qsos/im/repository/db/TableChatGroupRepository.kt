package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatGroup

interface TableChatGroupRepository : JpaRepository<TableChatGroup, Long> {

    fun findBySessionId(sessionId: Long): TableChatGroup?
    fun findByName(name: String): List<TableChatGroup>
    fun findByNameLike(name: String): List<TableChatGroup>

}