package vip.qsos.im.component

import vip.qsos.im.model.AppException
import vip.qsos.im.model.db.TableUser
import javax.transaction.Transactional

interface UserManageComponent {

    @Transactional
    fun register(name: String, password: String): TableUser

    fun login(name: String, password: String): TableUser
    fun findByName(name: String): TableUser?
    fun findByNameLike(name: String): List<TableUser>
    fun findByImAccount(account: String): TableUser?
    /**分配账号*/
    @Throws(AppException::class)
    fun assignImAccount(user: TableUser): TableUser

}