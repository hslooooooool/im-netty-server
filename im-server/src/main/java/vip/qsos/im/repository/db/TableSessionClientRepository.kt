package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.model.db.TableSessionClient
import javax.transaction.Transactional

@Repository
interface TableSessionClientRepository : JpaRepository<TableSessionClient, Int> {

    fun findByAccount(account: String): TableSessionClient?

    fun findByNidOrAccount(nid: String, account: String): TableSessionClient?

    @Transactional
    fun deleteByAccount(account: String)

}