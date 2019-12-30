package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableChatSessionOfClient
import javax.transaction.Transactional

interface IChatSessionRepository : JpaRepository<TableChatSessionOfClient, Int> {

    fun findByAccount(account: String): TableChatSessionOfClient?

    fun findByNid(nid: String): TableChatSessionOfClient?

    @Transactional
    fun deleteByAccount(account: String)

}