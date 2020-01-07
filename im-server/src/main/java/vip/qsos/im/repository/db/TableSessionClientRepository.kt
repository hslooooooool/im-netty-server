package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableSessionClient
import javax.transaction.Transactional

interface TableSessionClientRepository : JpaRepository<TableSessionClient, Int> {

    fun findByAccount(account: String): TableSessionClient?

    fun findByNid(nid: String): TableSessionClient?

    @Transactional
    fun deleteByAccount(account: String)

}