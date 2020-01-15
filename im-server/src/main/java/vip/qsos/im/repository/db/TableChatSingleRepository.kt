package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.model.db.TableChatSessionOfSingle

@Repository
interface TableChatSingleRepository : JpaRepository<TableChatSessionOfSingle, Long> {

    fun findBySessionId(sessionId: Long): TableChatSessionOfSingle?

}