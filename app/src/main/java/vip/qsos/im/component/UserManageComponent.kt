package vip.qsos.im.component

import vip.qsos.im.model.AppException
import vip.qsos.im.model.AppUser
import vip.qsos.im.model.LoginUser
import vip.qsos.im.model.db.TableFriend
import vip.qsos.im.model.db.TableUser
import javax.transaction.Transactional

@Transactional
interface UserManageComponent {

    fun register(name: String, password: String): LoginUser
    fun login(name: String, password: String): LoginUser

    fun findAll(): List<AppUser>
    fun findByName(name: String): AppUser?
    fun findById(userId: Long): AppUser
    fun findMine(userId: Long): AppUser
    fun findByNameLike(name: String): List<AppUser>
    fun findByImAccount(account: String): AppUser?
    /**分配账号*/
    @Throws(AppException::class)
    fun assignImAccount(user: TableUser): TableUser

    /**添加好友*/
    fun addFriend(userId: Long, friendId: Long): TableFriend

}