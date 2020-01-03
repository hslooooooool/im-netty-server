package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatClient
import javax.transaction.Transactional

interface IChatSessionRepository : JpaRepository<TableChatClient, Int> {

    fun findByAccount(account: String): TableChatClient?

    fun findByNid(nid: String): TableChatClient?

    @Transactional
    fun deleteByAccount(account: String)

}