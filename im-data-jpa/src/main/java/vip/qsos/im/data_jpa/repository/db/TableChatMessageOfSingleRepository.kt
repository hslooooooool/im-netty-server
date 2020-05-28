package vip.qsos.im.data_jpa.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.data_jpa.model.table.TableChatMessageOfSingle

@Repository
interface TableChatMessageOfSingleRepository : JpaRepository<TableChatMessageOfSingle, Long> {

    fun findBySessionIdAndTimelineBetween(sessionId: Long, startTimeline: Long, endTimeline: Long): List<TableChatMessageOfSingle>

}