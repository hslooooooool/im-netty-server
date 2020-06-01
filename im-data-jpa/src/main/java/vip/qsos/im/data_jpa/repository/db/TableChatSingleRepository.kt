package vip.qsos.im.data_jpa.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.data_jpa.model.table.TableChatSessionOfSingle

@Repository
interface TableChatSingleRepository : JpaRepository<TableChatSessionOfSingle, Long> {

    fun findBySessionId(sessionId: Long): TableChatSessionOfSingle?

}