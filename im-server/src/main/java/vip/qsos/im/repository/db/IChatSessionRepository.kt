package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatSession
import javax.transaction.Transactional

interface IChatSessionRepository : JpaRepository<TableChatSession, Int> {

    fun findByAccount(account: String): TableChatSession?

    fun findByNid(nid: String): TableChatSession?

    @Transactional
    fun deleteByAccount(account: String)

}