package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import vip.qsos.im.model.db.TableUser

interface TableUserRepository : JpaRepository<TableUser, Long> {

    fun findByName(name: String): TableUser?
    fun findByNameLike(name: String): List<TableUser>
    fun findByImAccount(account: String): TableUser?

}