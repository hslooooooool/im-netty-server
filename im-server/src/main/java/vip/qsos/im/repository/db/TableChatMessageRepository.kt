package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatMessage

interface TableChatMessageRepository : JpaRepository<TableChatMessage, Long> {

    fun findBySender(sender: String): TableChatMessage?

}