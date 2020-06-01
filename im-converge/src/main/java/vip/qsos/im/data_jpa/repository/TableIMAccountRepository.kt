package vip.qsos.im.data_jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.model.db.TableIMAccount

@Repository
interface TableIMAccountRepository : JpaRepository<TableIMAccount, Int> {

    fun findTopByUsed(used: Boolean): TableIMAccount?
    fun findByAccount(account: String): TableIMAccount?
    fun findAllByUsed(used: Boolean): List<TableIMAccount>

}