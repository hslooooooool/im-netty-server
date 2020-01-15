package vip.qsos.im.repository.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import vip.qsos.im.model.db.TableUser

@Repository
interface TableUserRepository : JpaRepository<TableUser, Long> {

    fun findByName(name: String): TableUser?
    fun findByNameLike(name: String): List<TableUser>
    fun findByImAccount(account: String): TableUser?

}