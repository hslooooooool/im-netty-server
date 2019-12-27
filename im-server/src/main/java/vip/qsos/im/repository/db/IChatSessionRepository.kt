package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatSession

interface IChatSessionRepository : JpaRepository<TableChatSession, Int> {

    fun findByAccount(account: String): TableChatSession?

    fun deleteByAccount(account: String)
}