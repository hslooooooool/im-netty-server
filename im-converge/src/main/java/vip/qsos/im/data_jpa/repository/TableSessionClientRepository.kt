package vip.qsos.im.data_jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.model.db.TableIMSession
import javax.transaction.Transactional

@Repository
interface TableSessionClientRepository : JpaRepository<TableIMSession, Int> {

    fun findByAccount(account: String): TableIMSession?

    fun findByNidOrAccount(nid: String, account: String): TableIMSession?

    @Transactional
    fun deleteByAccount(account: String)

}