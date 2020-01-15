package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.model.db.TableChatAccount

@Repository
interface TableChatAccountRepository : JpaRepository<TableChatAccount, Int> {

    fun findTopByUsed(used: Boolean): TableChatAccount?
    fun findByAccount(account: String): TableChatAccount?
    fun findAllByUsed(used: Boolean): List<TableChatAccount>

}