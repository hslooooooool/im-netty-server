package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatSingle

interface TableChatSingleRepository : JpaRepository<TableChatSingle, Long> {

    fun findBySessionId(sessionId: Long): TableChatSingle?

}